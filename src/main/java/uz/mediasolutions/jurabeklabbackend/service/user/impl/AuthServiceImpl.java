package uz.mediasolutions.jurabeklabbackend.service.user.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignInDTO;
import uz.mediasolutions.jurabeklabbackend.payload.req.SignUpDTO;
import uz.mediasolutions.jurabeklabbackend.payload.res.TokenDTO;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.secret.JwtService;
import uz.mediasolutions.jurabeklabbackend.service.user.abs.AuthService;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> signIn(String lang, SignInDTO dto) {
        if (dto.getOtp() == null || dto.getOtp().isEmpty()) {
            if (!userRepository.existsByPhoneNumber(dto.getPhoneNumber())) {
                User user = User.builder()
                        .phoneNumber(dto.getPhoneNumber())
                        .accountNonExpired(true)
                        .accountNonLocked(true)
                        .credentialsNonExpired(true)
                        .enabled(true)
                        .role(RoleName.ROLE_USER)
                        .registered(false)
                        .language(getLanguage(lang))
                        .build();
                userRepository.save(user);
            }
            //todo "otp should be sent"
            return ResponseEntity.ok("OTP is sent");
        }

        if (dto.getOtp().equals("0000")) { //todo here should check otp
            User user = userRepository.findByPhoneNumber(dto.getPhoneNumber());
            if (user.isRegistered()) {
                return ResponseEntity.ok(getToken(user));
            } else {
                return ResponseEntity.ok("Go to sign up");
            }
        }
        return ResponseEntity.status(401).body("Invalid OTP");
    }

    @Override
    public ResponseEntity<TokenDTO> signUp(String lang, SignUpDTO dto) {
        User user = userRepository.findByPhoneNumber(dto.getPhoneNumber());
        user.setRegistered(true);
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        userRepository.save(user);
        return ResponseEntity.ok(getToken(user));
    }

    private TokenDTO getToken(User user) {
        String accessToken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new TokenDTO("bearer", accessToken, refreshToken);
    }

    private String getLanguage(String lang) {
        return switch (lang) {
            case "ru" -> "ru";
            case "уз" -> "уз";
            default -> "uz";
        };
    }
}
