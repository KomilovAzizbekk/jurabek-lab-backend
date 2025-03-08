package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface OrderProductDTO {

    Long getId();

    String getName();

    Integer getQuantity();

    String getImageUrl();

    BigDecimal getPrice();

    Integer getDiscountPercent();

}
