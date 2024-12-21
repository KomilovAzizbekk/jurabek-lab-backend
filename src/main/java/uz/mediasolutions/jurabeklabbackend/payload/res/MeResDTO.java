package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.*;

import java.math.BigDecimal;
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

    private boolean blocked;

    private String lastName;

    private String username;

    private String role;

    private Integer notifications;

    private String androidVersion;

    private String iosVersion;

    private String androidUrl;

    private String iosUrl;

    private BigDecimal minOrderPrice;

}
