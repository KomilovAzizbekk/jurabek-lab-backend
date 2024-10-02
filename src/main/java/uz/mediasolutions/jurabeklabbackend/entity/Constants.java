package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;

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

    private String version;

}
