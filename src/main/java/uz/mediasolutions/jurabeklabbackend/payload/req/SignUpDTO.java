package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDTO {

    @NotBlank(message = "enter phone number")
    private String phoneNumber;

    @NotBlank(message = "enter first name")
    private String firstName;

    @NotBlank(message = "enter first name")
    private String lastName;

}
