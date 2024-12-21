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

    @Column(name = "translate")
    private String translate;

    @Column(name = "description")
    private String description;

    @Column(precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "deleted")
    private boolean deleted;

    @Column(name = "is_active")
    private boolean isActive;

}
