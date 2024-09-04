package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.jurabeklabbackend.entity.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByUsernameAndEnabledIsTrueAndAccountNonExpiredIsTrueAndAccountNonLockedIsTrueAndCredentialsNonExpiredIsTrue(String username);

    Optional<User> findFirstByIdAndEnabledIsTrueAndAccountNonExpiredIsTrueAndAccountNonLockedIsTrueAndCredentialsNonExpiredIsTrue(UUID userId);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);
}
