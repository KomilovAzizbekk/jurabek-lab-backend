package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "products")
@Builder
@EqualsAndHashCode(callSuper = true)
public class Product extends AbsLongDef {

    private String name;

    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private Integer stock;

    private String imageUrl;

    private Integer measurement;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "measurement_unit_id")
    private MeasurementUnit measurementUnit;

}
