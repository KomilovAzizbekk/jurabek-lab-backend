package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "constants")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer productPercent;

    private Integer cashbackPercent;

    private String androidVersion;

    private String iosVersion;

    private String androidUrl;

    private String iosUrl;

    private BigDecimal minOrderPrice;

    public void setMinOrderPrice(Integer integer) {
        this.minOrderPrice = BigDecimal.valueOf(integer);
    }
}
