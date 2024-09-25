package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsUUID;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionStatus;
import uz.mediasolutions.jurabeklabbackend.enums.TransactionType;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "transactions")
@Builder
@EqualsAndHashCode(callSuper = false)
@DynamicUpdate
@DynamicInsert
public class Transaction extends AbsUUID {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "serial")
    private Long number;

    private Long pharmacyId;

    private String pharmacyName; //Statik bo'lishi uchun

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

}
