package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class NotificationResDTO {

    private Long id;

    private boolean viewed;

    private String orderId;

    private String transactionId;

    private BigDecimal amount;

    private String cardNumber;

    private String cardName;

    private String type;

    private Timestamp createdTime;

}
