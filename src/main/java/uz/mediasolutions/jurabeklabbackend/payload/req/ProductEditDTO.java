package uz.mediasolutions.jurabeklabbackend.payload.req;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProductEditDTO {

    private String description;

    private String imageUrl;

    private Boolean isActive;

}
