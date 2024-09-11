package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface TrRequestDTO {

    String getCard();

    BigDecimal getAmount();

    String getStatus();

    String getPhoneNumber();

}
