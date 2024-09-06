package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PharmacyReqDTO {

    @NotBlank
    private String name;

    @NotBlank
    private String address;

}
