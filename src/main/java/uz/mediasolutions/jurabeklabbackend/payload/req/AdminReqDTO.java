package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminReqDTO {

    @NotBlank
    private String role;

    @NotBlank
    private String username;

    @NotBlank
    private String password;

}
