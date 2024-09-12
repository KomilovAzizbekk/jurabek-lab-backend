package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PharmacyReqDTO {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String address;

}
