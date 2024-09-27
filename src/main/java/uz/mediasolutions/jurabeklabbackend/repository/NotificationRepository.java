package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Notification;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.NotificationDTO;

import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification, Long> {

    int countAllByUserIdAndViewedFalse(UUID userId);

    @Query(value = "SELECT n.id,\n" +
            "       n.viewed,\n" +
            "       n.order_id       as orderId,\n" +
            "       n.transaction_id as transactionId,\n" +
            "       n.amount,\n" +
            "       n.card_number    as cardNumber,\n" +
            "       n.card_name      as cardName,\n" +
            "       n.type\n" +
            "FROM notifications n\n" +
            "         LEFT JOIN users u ON n.user_id = u.id\n" +
            "WHERE u.id = :userId", nativeQuery = true)
    Page<NotificationDTO> findMyNotifications(@Param("userId") UUID userId,
                                              Pageable pageable);

}
