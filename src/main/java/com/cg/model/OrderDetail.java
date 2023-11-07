package com.cg.model;

import com.cg.orderDetail.dto.OrderDetailResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "order_detail")
@Accessors(chain = true)
public class OrderDetail extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id",referencedColumnName = "id",nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id",referencedColumnName = "id",nullable = false)
    private Product product;

    @Column
    private Long quantity;

    @Column
    private BigDecimal price;

    @Column
    private BigDecimal amount;

    @Column
    private String note;


    public OrderDetailResult toOrderDetailDTO() {
        return new OrderDetailResult()
                .setOrderDetailId(id)
                .setProduct(product.toProductDTO())
                .setQuantity(String.valueOf(quantity))
                .setPrice(price)
                .setAmount(amount)
                .setNote(note)
                ;
    }
}
