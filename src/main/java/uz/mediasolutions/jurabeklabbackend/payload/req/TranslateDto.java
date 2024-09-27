package uz.mediasolutions.jurabeklabbackend.payload.req;

import lombok.Data;

@Data
public class TranslateDto {

    private Long id;

    private String key;

    private String textUz;

    private String textRu;

    private String textKr;
}