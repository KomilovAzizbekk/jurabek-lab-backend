package uz.mediasolutions.jurabeklabbackend.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import uz.mediasolutions.jurabeklabbackend.entity.Product;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.Product2DTO;
import uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO.ProductDTO;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.price,\n" +
            "       p.image_url as imageUrl\n" +
            "FROM products p\n" +
            "WHERE (:search IS NULL OR p.name ILIKE '%' || :search || '%') ||\n" +
            "      (:search IS NULL OR p.translate ILIKE '%' || :search || '%')\n" +
            "  AND p.deleted = false\n" +
            "ORDER BY p.created_at DESC", nativeQuery = true)
    Page<ProductDTO> findAllWithSearch(@Param("search") String search,
                                       Pageable pageable);

    @Query(value = "SELECT p.id,\n" +
            "       p.name,\n" +
            "       p.price                 as pricePerProduct,\n" +
            "       op.quantity,\n" +
            "       (p.price * op.quantity) as price,\n" +
            "       p.image_url as imageUrl\n" +
            "FROM products p\n" +
            "         LEFT JOIN order_products op ON op.product_id = p.id\n" +
            "WHERE op.order_id = :orderId", nativeQuery = true)
    Page<Product2DTO> findAllByOrderId(@Param("orderId") Long orderId,
                                       Pageable pageable);

    boolean existsByNameAndDeletedFalse(String name);

    @Query(value = "UPDATE products SET price = :price WHERE name = :name AND deleted = false", nativeQuery = true)
    Product setPriceByName(@Param("name") String name,
                           @Param("price") BigDecimal price);

    Optional<Product> findByIdAndDeletedFalse(Long id);

    Optional<Product> findByNameAndDeletedFalse(String name);
}
