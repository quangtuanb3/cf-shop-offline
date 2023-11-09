package com.cg.product.dto;

import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.ProductAvatar;
import com.cg.category.dto.CategoryResult;
import com.cg.productAvatar.dto.ProductAvatarResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;


@Getter
@Setter
@Accessors(chain = true)
public class ProductResult {
    private String id;
    private String title;
    private BigDecimal price;
    private String unit;
    private CategoryResult category;
    private String productAvatarId;
    private String productAvatarUrl;
}
