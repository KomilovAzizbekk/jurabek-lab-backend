package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import uz.mediasolutions.jurabeklabbackend.entity.RefreshToken;

import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    boolean existsByUserId(UUID id);

    RefreshToken findByUserId(UUID id);

    @Query(value = "DELETE FROM refresh_token WHERE user_id = :user_id", nativeQuery = true)
    void deleteByUserId(UUID id);
}
