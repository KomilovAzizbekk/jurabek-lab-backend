package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class WithdrawReqDTO {

    @NotNull
    private UUID cardId;

    @NotNull
    private BigDecimal amount;

}
