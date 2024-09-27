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

    boolean existsByPhoneNumberAndDeletedFalse(String phoneNumber);

    Optional<User> findByPhoneNumberAndDeletedFalse(String phoneNumber);

    @Query(value = "SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END\n" +
            "FROM users u\n" +
            "WHERE u.username = :username\n" +
            "  AND u.id != :id\n" +
            "  AND u.deleted = false", nativeQuery = true)
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
            "  AND u.deleted = false\n" +
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
            "  AND u.deleted = false\n" +
            "ORDER BY u.created_at DESC", nativeQuery = true)
    Page<AdminDTO> getAllAdmins(Pageable pageable,
                                @Param("search") String search);

    boolean existsByUsernameAndDeletedFalse(String username);

    Optional<User> findByUsernameAndDeletedFalse(String username);

    @Query(value = "SELECT u\n" +
            "FROM users u\n" +
            "         LEFT JOIN transactions t ON u.id = t.user_id\n" +
            "WHERE t.id = :transactionId\n", nativeQuery = true)
    Optional<User> findByTransactionId(@Param("transactionId") UUID transactionId);

}
