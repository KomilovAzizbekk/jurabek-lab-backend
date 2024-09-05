package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsUUID;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
@Builder
@EqualsAndHashCode(callSuper = false)
public class Transaction extends AbsUUID {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

}
