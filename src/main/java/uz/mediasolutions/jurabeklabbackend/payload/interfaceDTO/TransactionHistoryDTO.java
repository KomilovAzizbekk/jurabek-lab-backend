package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface TransactionHistoryDTO {

    BigDecimal getAmount();

    String getType();

    String getStatus();

    String getCardNumber();

    String getCardName();

    String getPharmacy();

    Long getNumber();

    Timestamp getUpdatedTime();

}
