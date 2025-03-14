package uz.mediasolutions.jurabeklabbackend.payload.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ConstantsDTO {

    private Integer productPercent;

    private Integer cashbackPercent;

    private String androidVersion;

    private String iosVersion;

    private String androidUrl;

    private String iosUrl;

    private Integer minOrderPrice;

    private Integer minTransactionSum;
}
