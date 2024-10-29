package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface ProductDTO {

    Long getId();

    String getName();

    BigDecimal getPrice();

    String getImageUrl();

    String getDescription();

}
