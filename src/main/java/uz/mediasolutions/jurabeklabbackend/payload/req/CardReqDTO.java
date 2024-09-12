package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CardReqDTO {

    @NotNull
    private String cardNumber;

    private String name;

}
