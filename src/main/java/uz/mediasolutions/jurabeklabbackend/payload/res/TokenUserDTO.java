package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenUserDTO {

    private String tokenType;

    private String accessToken;

    private String refreshToken;

    private UUID id;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String role;
}
