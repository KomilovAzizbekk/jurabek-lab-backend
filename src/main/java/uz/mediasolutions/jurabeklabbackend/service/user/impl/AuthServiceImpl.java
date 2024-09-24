package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.hibernate.internal.util.compare.CalendarComparator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.entity.RefreshToken;
import uz.mediasolutions.jurabeklabbackend.entity.SmsInfo;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.exceptions.RestException;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenUserDTO;
import uz.mediasolutions.jurabeklabbackend.repository.RefreshTokenRepository;
import uz.mediasolutions.jurabeklabbackend.repository.SmsInfoRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.secret.JwtService;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.AuthService;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service("userAuthService")
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsService smsService;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    private final SmsInfoRepository smsInfoRepository;

    String message = "<#> Jurabek Lab mobil ilovasidan ro‘yxatdan o‘tish uchun tasdiqlash kodi: ";

    @Value("${sms.callback.url}")
    private String callbackUrl;

    @Override
    public ResponseEntity<?> signIn(String lang, SignInDTO dto) {
        User existingUser;
        // Faqat phone number junatilganda ushbu if ishlaydi
        if (dto.getOtp() == null || dto.getOtp().isEmpty()) {
            if (!userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
                User user = User.builder()
                        .phoneNumber(dto.getPhoneNumber())
                        .role(RoleName.ROLE_USER)
                        .registered(false)
                        .balance(new BigDecimal(0))
                        .language(getLanguage(lang))
                        .build();
                existingUser = userRepository.save(user);
            } else {
                existingUser = userRepository.findByPhoneNumber(dto.getPhoneNumber()).orElseThrow(
                        () -> RestException.restThrow("User not found", HttpStatus.NOT_FOUND)
                );
            }

            // Foydalanuvchi 2 daqiqa ichida kod yuborishdan foydalanganligini tekshirish
            if (existingUser.getLastOtpTime() != null) {
                System.out.println("Salom");
                LocalDateTime lastOtpTime = existingUser.getLastOtpTime();
                LocalDateTime applicableTime = lastOtpTime.plusMinutes(2);
                System.out.println(lastOtpTime);
                System.out.println(applicableTime);
                System.out.println(LocalDateTime.now());
                if (LocalDateTime.now().isBefore(applicableTime)) {
                    Duration duration = Duration.between(LocalDateTime.now(), applicableTime);
                    throw RestException.restThrow("There should be at least 2 minutes between every sms. " +
                            "Remaining time: " +
                            duration.getSeconds() + " seconds",
                            HttpStatus.BAD_REQUEST);
                }
            }

            // Sending otp
            try {
                Random rand = new Random();
                String otp = String.format("%04d", rand.nextInt(10000));

                existingUser.setOtp(otp);
                existingUser.setLastOtpTime(LocalDateTime.now());
                userRepository.save(existingUser);

                HttpStatusCode statusCode = smsService.sendSms(dto.getPhoneNumber(), message + otp, "4546", callbackUrl);

                if (statusCode == HttpStatus.OK) {
                    return ResponseEntity.ok("OTP is sent");
                } else if (statusCode == HttpStatus.UNAUTHORIZED) {
                    smsService.refreshToken();
                    smsService.sendSms(dto.getPhoneNumber(), message + otp, "4546", callbackUrl);
                } else {
                    throw RestException.restThrow("Error with sending OTP", HttpStatus.INTERNAL_SERVER_ERROR);
                }


                // 2 daqiqadan so'ng OTPni bazadan o'chirish uchun vazifani rejalashtirish
                scheduler.schedule(() -> {
                    try {
                        existingUser.setOtp(null); // OTPni o'chirish
                        userRepository.save(existingUser); // O'chirilgan OTPni saqlash
                        System.out.println("OTP o'chirildi.");
                    } catch (Exception e) {
                        System.err.println("OTP o'chirishda xato: " + e.getMessage());
                    }
                }, 2, TimeUnit.MINUTES); // 2 daqiqadan so'ng bajarish
            } catch (Exception e) {
                e.printStackTrace();
                throw RestException.restThrow("Error with sending OTP", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        // Agar phone number va otp junatilganda ushbu if ishlaydi. Yani sms junatilgandan so'ng
        User u = userRepository.findByPhoneNumber(dto.getPhoneNumber()).orElseThrow(
                () -> RestException.restThrow("User not found", HttpStatus.NOT_FOUND)
        );

        if (dto.getOtp().equals(u.getOtp())) {
            User user = userRepository.findByPhoneNumber(dto.getPhoneNumber()).orElseThrow(
                    () -> RestException.restThrow("Phone number not found", HttpStatus.NOT_FOUND)
            );

            user.setOtp(null);
            userRepository.save(user);

            if (user.isRegistered()) {
                return ResponseEntity.ok(getTokenForUser(user));
            } else {
                return ResponseEntity.ok("Go to sign up");
            }
        }
        return ResponseEntity.status(401).body("Invalid OTP");
    }

    @Override
    public ResponseEntity<TokenUserDTO> signUp(String lang, SignUpDTO dto) {
        User user = userRepository.findByPhoneNumber(dto.getPhoneNumber()).orElseThrow(
                () -> RestException.restThrow("Phone number not found", HttpStatus.NOT_FOUND)
        );
        user.setRegistered(true);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        userRepository.save(user);
        return ResponseEntity.ok(getTokenForUser(user));
    }

    @Override
    public ResponseEntity<?> logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (user == null) {
            throw RestException.restThrow("User not found", HttpStatus.NOT_FOUND);
        }
        try {
            RefreshToken token = refreshTokenRepository.findByUserId(user.getId());
            refreshTokenRepository.deleteById(token.getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(Rest.DELETED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Rest.ERROR);
        }
    }

    @Override
    public void saveSmsInfo(Map<String, Object> callbackData) {
        // Olingan ma'lumotlarni ishlash uchun logika qo'shish
        String requestId = (String) callbackData.get("request_id");
        String messageId = (String) callbackData.get("message_id");
        String userSmsId = (String) callbackData.get("user_sms_id");
        String country = (String) callbackData.get("country");
        String phoneNumber = (String) callbackData.get("phone_number");
        Integer smsCount = (Integer) callbackData.get("sms_count");
        String status = (String) callbackData.get("status");
        Timestamp statusDate = (Timestamp) callbackData.get("status_date");

        SmsInfo smsInfo = SmsInfo.builder()
                .requestId(requestId)
                .messageId(messageId)
                .userSmsId(userSmsId)
                .country(country)
                .phoneNumber(phoneNumber)
                .smsCount(smsCount)
                .status(status)
                .statusDate(statusDate)
                .build();

        smsInfoRepository.save(smsInfo);
    }

    private TokenUserDTO getTokenForUser(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        //Saving refresh token to database
        RefreshToken token;
        if (refreshTokenRepository.existsByUserId(user.getId())) {
            token = refreshTokenRepository.findByUserId(user.getId());
            token.setExpireDate(jwtService.extractExpiration(refreshToken));
            token.setToken(passwordEncoder.encode(refreshToken));
        } else {
            token = RefreshToken.builder()
                    .token(passwordEncoder.encode(refreshToken))
                    .user(user)
                    .expireDate(jwtService.extractExpiration(refreshToken))
                    .build();
        }
        refreshTokenRepository.save(token);

        return new TokenUserDTO("Bearer", accessToken, refreshToken, user.getId(),
                user.getPhoneNumber(), user.getFirstName(), user.getLastName(), user.getRole().name());
    }

    private String getLanguage(String lang) {
        return switch (lang) {
            case "ru" -> "ru";
            case "kr" -> "kr";
            default -> "uz";
        };
    }
}
