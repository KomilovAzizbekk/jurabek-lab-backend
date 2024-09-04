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
@Table(name = "orders")
@Builder
@EqualsAndHashCode(callSuper = true)
public class Order extends AbsLongDef {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
