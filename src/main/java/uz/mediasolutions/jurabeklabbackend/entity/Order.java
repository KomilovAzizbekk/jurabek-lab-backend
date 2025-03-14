package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongAudit;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "orders")
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
public class Order extends AbsLongAudit {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(precision = 12, scale = 2)
    private BigDecimal totalPrice;

    private String pharmacyPhoneNumber;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @Column(nullable = false)
    private Long pharmacyId;

    @Column(nullable = false)
    private String pharmacyName; //Statik bo'lishi kerakligi uchun

    private Timestamp acceptedTime;

}
