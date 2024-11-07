package uz.mediasolutions.jurabeklabbackend.payload.res;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SmsTokenResDTO {

    private String message;

    private String token_type;

    private DataRes data;

}
