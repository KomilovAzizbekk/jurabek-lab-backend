package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;
import uz.mediasolutions.jurabeklabbackend.enums.NotificationType;

import java.math.BigDecimal;

@Entity
@Table(name = "notifications")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(callSuper = true)
public class Notification extends AbsLongDef {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private boolean viewed;

    private String orderId;

    private String transactionId;

    private BigDecimal amount;

    private String cardNumber;

    private String cardName;

    @Enumerated(EnumType.STRING)
    private NotificationType type;

}
