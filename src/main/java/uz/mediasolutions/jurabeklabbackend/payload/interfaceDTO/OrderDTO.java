package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;

public interface OrderDTO {

    Long getId();

    String getStatus();

    Timestamp getCreatedTime();

    Timestamp getAcceptedTime();

    BigDecimal getTotalPrice();

    String getPharmacy();

    String getPhoneNumber();

}
