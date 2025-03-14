package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "pharmacies")
@Builder
@EqualsAndHashCode(callSuper = true)
@DynamicInsert
@DynamicUpdate
public class Pharmacy extends AbsLongDef {

    @Column(nullable = false)
    private String name;

    @Column(name = "enable_order")
    private Boolean enableOrder;

    @Column(nullable = false)
    private String address;

    @ManyToOne
    @JoinColumn(name = "district_id")
    private District district;

    private String inn;

    private boolean deleted;

}
