package uz.mediasolutions.jurabeklabbackend.payload.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileReqDTO {

    private String firstName;

    private String lastName;

}
