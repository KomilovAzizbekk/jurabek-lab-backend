package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OrderProductDTO {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

}
