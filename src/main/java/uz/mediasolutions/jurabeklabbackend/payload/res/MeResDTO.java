package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.*;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MeResDTO {

    private UUID id;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private String username;

    private String role;

    private Integer notifications;

    private String androidVersion;

    private String iosVersion;

    private String androidUrl;

    private String iosUrl;

}
