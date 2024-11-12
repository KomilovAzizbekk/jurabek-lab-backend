package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface Order2DTO {

    Long getId();

    String getStatus();

    String getInn();

    Timestamp getCreatedTime();

    BigDecimal getTotalPrice();

    String getPharmacy();

    String getPhoneNumber();

    String getAddress();

}
