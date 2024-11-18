package uz.mediasolutions.jurabeklabbackend.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mediasolutions.jurabeklabbackend.entity.Constants;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.repository.ConstantsRepository;
import uz.mediasolutions.jurabeklabbackend.repository.SmsTokenRepository;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;
import uz.mediasolutions.jurabeklabbackend.service.user.impl.SmsService;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final SmsTokenRepository tokenRepository;
    private final SmsService smsService;
    private final ConstantsRepository constantsRepository;

    @Value("${spring.sql.init.mode}")
    private String mode;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            addAdmin();
        }

        if (tokenRepository.countSmsToken() != 1) {
            smsService.obtainToken();
        }

        addTestUser();
        addConstants();
    }

    private void addConstants() {
        if (!constantsRepository.existsById(1L)) {
            Constants constants = Constants.builder()
                    .cashbackPercent(10)
                    .productPercent(10)
                    .androidVersion("1.0")
                    .iosVersion("1.0")
                    .build();
            constantsRepository.save(constants);
        }
    }

    private void addTestUser() {
        if (userRepository.existsByPhoneNumberAndDeletedFalse("+998 00 000-00-00")) {
            User user = User.builder()
                    .deleted(false)
                    .balance(new BigDecimal(0))
                    .role(RoleName.ROLE_USER)
                    .language("uz")
                    .registered(true)
                    .blocked(false)
                    .firstName("test")
                    .lastName("test")
                    .phoneNumber("+998 00 000-00-00")
                    .build();
            userRepository.save(user);
        }
    }

    private void addAdmin() {
        User superAdmin = User.builder()
                .role(RoleName.ROLE_SUPER_ADMIN)
                .username("jurabek")
                .deleted(false)
                .password(passwordEncoder.encode("Qwerty123@"))
                .build();
        userRepository.save(superAdmin);
    }

}
