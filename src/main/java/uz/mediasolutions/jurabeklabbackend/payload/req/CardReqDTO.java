package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardReqDTO {

    @NotBlank
    private String cardNumber;

    private String name;

}
