package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import uz.mediasolutions.jurabeklabbackend.utills.constants.Rest;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SignUpDTO {

    @NotNull
    @NotBlank(message = "enter phone number")
    @Pattern(regexp = Rest.PHONE_NUMBER_REGEX, message = "phone number format error")
    private String phoneNumber;

    @NotNull
    @NotBlank(message = "enter first name")
    private String firstName;

    @NotNull
    @NotBlank(message = "enter first name")
    private String lastName;

}
