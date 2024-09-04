package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TokenDTO {

    private String tokenType;

    private String accessToken;

    private String refreshToken;
}
