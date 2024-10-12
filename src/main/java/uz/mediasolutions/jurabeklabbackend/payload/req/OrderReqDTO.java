package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderReqDTO {

    @NotNull
    private Long pharmacyId;

    @NotNull
    @NotBlank
    @Pattern(regexp = Rest.PHONE_NUMBER_REGEX)
    private String pharmacyPhoneNumber;

    private String inn;

    @Valid
    @NotNull
    private List<OrderProductReqDTO> products;

    @NotNull
    private BigDecimal totalPrice;

}
