package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
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
@DynamicUpdate
@DynamicInsert
public class Product extends AbsLongDef {

    @Column(nullable = false)
    private String name;

    private String translate;

    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    private String imageUrl;

    private boolean deleted;

}
