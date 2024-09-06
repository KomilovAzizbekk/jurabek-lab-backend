package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;
import uz.mediasolutions.jurabeklabbackend.enums.OrderStatus;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pharmacies")
@Builder
@EqualsAndHashCode(callSuper = true)
public class Pharmacy extends AbsLongDef {

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

}
