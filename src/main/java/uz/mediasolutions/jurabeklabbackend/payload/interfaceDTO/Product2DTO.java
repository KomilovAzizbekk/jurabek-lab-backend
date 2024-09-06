package uz.mediasolutions.jurabeklabbackend.payload.interfaceDTO;

import java.math.BigDecimal;

public interface Product2DTO {

    Long getId();

    String getName();

    Integer getQuantity();

    BigDecimal getPricePerProduct();

    BigDecimal getPrice();

    String getImageUrl();

}
