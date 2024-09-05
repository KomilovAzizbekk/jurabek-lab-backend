package uz.mediasolutions.jurabeklabbackend.component;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.enums.RoleName;
import uz.mediasolutions.jurabeklabbackend.repository.UserRepository;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.sql.init.mode}")
    private String mode;

    @Override
    public void run(String... args) throws Exception {
        if (mode.equals("always")) {
            addAdmin();
        }


    }

    private void addAdmin() {
        User superAdmin = User.builder()
                .role(RoleName.ROLE_SUPER_ADMIN)
                .username("jurabek")
                .password(passwordEncoder.encode("Qwerty123@"))
                .build();
        userRepository.save(superAdmin);
    }

}
