package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import uz.mediasolutions.jurabeklabbackend.entity.OrderProduct;

import java.util.List;
import java.util.Optional;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

    List<OrderProduct> findAllByOrderId(Long orderId);

    boolean existsByOrderId(Long orderId);

}
