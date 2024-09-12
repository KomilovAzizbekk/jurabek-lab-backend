package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminReqDTO {

    @NotNull
    private String role;

    @NotNull
    private String username;

    @NotNull
    private String password;

}
