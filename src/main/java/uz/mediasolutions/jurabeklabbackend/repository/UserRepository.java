package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.User;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.AdminDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.UserDTO;

import java.sql.Timestamp;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findFirstByUsernameAndEnabledIsTrueAndAccountNonExpiredIsTrueAndAccountNonLockedIsTrueAndCredentialsNonExpiredIsTrue(String username);

    Optional<User> findFirstByIdAndEnabledIsTrueAndAccountNonExpiredIsTrueAndAccountNonLockedIsTrueAndCredentialsNonExpiredIsTrue(UUID userId);

    boolean existsByPhoneNumber(String phoneNumber);

    User findByPhoneNumber(String phoneNumber);

    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END\n" +
            "FROM users u WHERE u.username = :username AND u.id != :id", nativeQuery = true)
    boolean existsByUsernameAndNotId(@Param("username") String username,
                                     @Param("id") UUID id);

    @Query(value = "SELECT u.id,\n" +
            "       u.phone_number,\n" +
            "       u.first_name,\n" +
            "       u.last_name\n" +
            "FROM users u\n" +
            "WHERE (:search IS NULL\n" +
            "    OR u.phone_number ILIKE '%' || :search || '%'\n" +
            "    OR u.first_name ILIKE '%' || :search || '%'\n" +
            "    OR u.last_name ILIKE '%' || :search || '%')\n" +
            "  AND u.role = 'ROLE_USER'\n" +
            "ORDER BY u.created_at DESC", nativeQuery = true)
    Page<UserDTO> getAllUsers(Pageable pageable,
                              @Param("search") String search);

    @Query(value = "SELECT u.id,\n" +
            "       u.username,\n" +
            "       u.role\n" +
            "FROM users u\n" +
            "WHERE (:search IS NULL\n" +
            "    OR u.phone_number ILIKE '%' || :search || '%'\n" +
            "    OR u.role ILIKE '%' || :search || '%')\n" +
            "  AND u.role != 'ROLE_USER'\n" +
            "ORDER BY u.created_at DESC", nativeQuery = true)
    Page<AdminDTO> getAllAdmins(Pageable pageable,
                                @Param("search") String search);

    boolean existsByUsername(String username);

    @Query(value = "SELECT u.created_at FROM users u WHERE u.id = :id", nativeQuery = true)
    Timestamp getCreatedTime(@Param("id") UUID id);
}
