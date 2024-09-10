package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface TransactionHistoryDTO {

    BigDecimal getAmount();

    String getType();

    String getStatus();

    String getCard();



}
