package com.cg.model;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Setter
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

    @Column(name = "product_avatar_url", insertable = false, updatable = false)
    private String productAvatarUrl;

    @OneToOne
    @JoinColumn(name = "product_avatar_id", foreignKey = @ForeignKey(name = "fk_product_productAvatar"), referencedColumnName = "id", nullable = false)
    private ProductAvatar productAvatar;

    public Product setCategoryId(Long categoryId) {
        this.category = new Category(this.categoryId = categoryId);
        return this;
    }

    public Product setProductAvatar(ProductAvatar productAvatar) {
        this.productAvatarUrl = productAvatar.getFileUrl();
        this.productAvatar = productAvatar;
        return this;
    }
}
