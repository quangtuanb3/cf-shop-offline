package com.cg.model;



import com.cg.product.dto.ProductResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
@Entity
@Table(name = "products")
@Accessors(chain = true)
public class Product extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column(precision = 10, scale = 0, nullable = false)
    private BigDecimal price;
    private String unit;


    @ManyToOne
    @JoinColumn(name = "category_id",referencedColumnName = "id",nullable = false)
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToOne
    @JoinColumn(name = "product_avatar_id",referencedColumnName = "id" ,  nullable = false)
    private ProductAvatar productAvatar;


    public ProductResult toProductDTO() {
        return new ProductResult()
                 .setId(String.valueOf(id))
                 .setTitle(title)
                 .setPrice(price)
                 .setUnit(unit)
                 .setCategory(category.toCategoryDTO())
                 .setAvatar(productAvatar.toProductAvatarDTO())
                 ;
    }



    public ProductResult toDTO() {
        return new ProductResult()
                .setId(String.valueOf(id))
                .setTitle(title)
                .setPrice(price)
                .setUnit(unit)
                .setCategory(category.toDTO(category.getId(), category.getTitle()))
                .setAvatar(productAvatar.toProductAvatarResDTO());
    }
}
