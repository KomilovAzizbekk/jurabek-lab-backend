package uz.mediasolutions.jurabeklabbackend.payload.req;

import jakarta.validation.constraints.NotBlank;
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
public class SignInDTO {

    @NotBlank(message = "enter phone number")
    @Pattern(regexp = Rest.PHONE_NUMBER_REGEX, message = "phone number format error")
    private String phoneNumber;

    private String otp;

}
