package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;
import java.sql.Timestamp;

public interface Order2DTO {

    Long getId();

    String getStatus();

    String getInn();

    Timestamp getCreatedTime();

    Timestamp getUpdatedTime();

    BigDecimal getTotalPrice();

    String getPharmacy();

    String getRegion();

    String getPhoneNumber();

    String getAddress();

    String getUserPhone();

    String getFullName();

    String getUpdatedBy();

}
