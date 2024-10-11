package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;
import java.util.UUID;

public interface TrRequestDTO {

    UUID getId();

    String getCard();

    BigDecimal getAmount();

    String getStatus();

    String getNumber();

    String getPhoneNumber();

}
