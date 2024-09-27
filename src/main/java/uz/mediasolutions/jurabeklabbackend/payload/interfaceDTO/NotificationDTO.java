package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface NotificationDTO {

    Long getId();

    boolean isViewed();

    String getOrderId();

    String getTransactionId();

    BigDecimal getAmount();

    String getCardNumber();

    String getCardName();

    String getType();

}
