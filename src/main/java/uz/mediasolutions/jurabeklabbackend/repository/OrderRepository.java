package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderDTO;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o.id,\n" +
            "       o.status,\n" +
            "       o.created_at            as createdTime,\n" +
            "       o.accepted_time         as acceptedTime,\n" +
            "       o.total_price           as totalPrice,\n" +
            "       p.name,\n" +
            "       o.pharmacy_phone_number as phoneNumber\n" +
            "FROM orders o\n" +
            "         LEFT JOIN pharmacies p ON p.id = o.pharmacy_id\n" +
            "WHERE (:status IS NULL\n" +
            "    OR o.status = :status)\n" +
            "  AND o.user_id = :userId\n" +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    Page<OrderDTO> getAllByStatus(@Param("status") String status,
                                  @Param("userId") UUID userId,
                                  Pageable pageable);

    @Query(value = "SELECT o.id,\n" +
            "       o.status,\n" +
            "       o.created_at            as createdTime,\n" +
            "       o.accepted_time         as acceptedTime,\n" +
            "       o.total_price           as totalPrice,\n" +
            "       p.name,\n" +
            "       o.pharmacy_phone_number as phoneNumber\n" +
            "FROM orders o\n" +
            "         LEFT JOIN pharmacies p ON p.id = o.pharmacy_id\n" +
            "WHERE o.id = :id\n" +
            "  AND o.user_id = :userId\n" +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    OrderDTO getOrdersById(@Param("id") Long id,
                           @Param("userId") UUID userId);

}
