package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_products")
@Builder
@EqualsAndHashCode(callSuper = true)
public class OrderProducts extends AbsLongDef {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
