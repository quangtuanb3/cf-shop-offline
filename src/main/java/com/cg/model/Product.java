package com.cg.model;


import com.cg.category.CategoryMapper;
import com.cg.product.dto.ProductResult;
import lombok.*;
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

    @Setter(AccessLevel.NONE)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", foreignKey = @ForeignKey(name = "fk_product_category"))
    private Category category;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Long categoryId;

    public Product setCategoryId(Long categoryId) {
        this.category = new Category(this.categoryId = categoryId);
        return this;
    }

    @OneToMany(mappedBy = "product")
    private List<OrderDetail> orderDetails;

    @OneToOne
    @JoinColumn(name = "product_avatar_id", referencedColumnName = "id", nullable = false)
    private ProductAvatar productAvatar;


    public ProductResult toProductDTO() {
        return null;
    }
}
