package uz.mediasolutions.jurabeklabbackend.entity;

import jakarta.persistence.*;
import lombok.*;
import uz.mediasolutions.jurabeklabbackend.entity.template.AbsLongDef;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_products")
@Builder
@EqualsAndHashCode(callSuper = true)
public class OrderProduct extends AbsLongDef {

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    private Long productId;

    private String productName; //Statik bo'lishi uchun

    private Integer quantity;

}
