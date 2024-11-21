package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Order;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Order2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderDTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.OrderProductDTO;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(value = "SELECT o.id,\n" +
            "       o.status,\n" +
            "       o.created_at                       as createdTime,\n" +
            "       o.accepted_time                    as acceptedTime,\n" +
            "       o.total_price                      as totalPrice,\n" +
            "       p.name                             as pharmacy,\n" +
            "       o.pharmacy_phone_number            as phoneNumber,\n" +
            "       u.phone_number                     as userPhone,\n" +
            "       u.first_name || ' ' || u.last_name as fullName,\n" +
            "       p.address\n" +
            "FROM orders o\n" +
            "         LEFT JOIN pharmacies p ON p.id = o.pharmacy_id\n" +
            "         LEFT JOIN users u ON o.user_id = u.id\n" +
            "WHERE (:status IS NULL\n" +
            "    OR o.status = :status)\n" +
            "  AND o.user_id = :userId\n" +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    Page<OrderDTO> getAllByStatus(@Param("status") String status,
                                  @Param("userId") UUID userId,
                                  Pageable pageable);

    @Query(value = "SELECT o.id,\n" +
            "       o.status,\n" +
            "       o.created_at                       as createdTime,\n" +
            "       o.accepted_time                    as acceptedTime,\n" +
            "       o.total_price                      as totalPrice,\n" +
            "       p.name                             as pharmacy,\n" +
            "       o.pharmacy_phone_number            as phoneNumber,\n" +
            "       u.phone_number                     as userPhone,\n" +
            "       u.first_name || ' ' || u.last_name as fullName,\n" +
            "       p.address\n" +
            "FROM orders o\n" +
            "         LEFT JOIN pharmacies p ON p.id = o.pharmacy_id\n" +
            "         LEFT JOIN users u ON o.user_id = u.id\n" +
            "WHERE o.id = :id\n" +
            "  AND o.user_id = :userId\n" +
            "ORDER BY o.created_at DESC", nativeQuery = true)
    OrderDTO getOrdersById(@Param("id") Long id,
                           @Param("userId") UUID userId);

    @Query(value = """
            SELECT o.id,
                   o.status,
                   o.created_at                       as createdTime,
                   p.inn,
                   o.total_price                      as totalPrice,
                   p.name                             as pharmacy,
                   o.pharmacy_phone_number            as phoneNumber,
                   u.phone_number                     as userPhone,
                   u.first_name || ' ' || u.last_name as fullName,
                   p.address
            FROM orders o
                     LEFT JOIN pharmacies p ON p.id = o.pharmacy_id
                     LEFT JOIN users u ON o.user_id = u.id
            WHERE :status IS NULL
               OR o.status = :status
            ORDER BY o.created_at DESC
            """, nativeQuery = true)
    Page<Order2DTO> getAllOrders(@Param("status") String status,
                                 Pageable pageable);

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.price,\n" +
            "       p.image_url as imageUrl,\n" +
            "       op.quantity\n" +
            "FROM products p\n" +
            "         LEFT JOIN order_products op ON op.product_id = p.id\n" +
            "WHERE op.order_id = :orderId\n" +
            "ORDER BY p.name", nativeQuery = true)
    Page<OrderProductDTO> getOrderProducts(@Param("orderId") Long orderId,
                                           Pageable pageable);

    @Query(value = "SELECT o.* from orders o WHERE o.status = 'SENT' AND o.created_at < :cutoff_time", nativeQuery = true)
    List<Order> findPendingOrdersBefore(@Param("cutoff_time") Timestamp cutoffTime);

}
