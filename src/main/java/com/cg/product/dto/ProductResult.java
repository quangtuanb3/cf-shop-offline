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
@NoArgsConstructor
@Accessors(chain = true)
public class ProductResult {
    private String id;
    private String title;
    private BigDecimal price;
    private String unit;
    private CategoryResult category;
    private ProductAvatarResult avatar;

    public ProductResult(Long id, String title, BigDecimal price, String unit, Category category, ProductAvatar avatar) {
        this.id = id.toString();
        this.title = title;
        this.price = price;
        this.unit = unit;
        this.category = new CategoryResult(category.getId(), category.getTitle());
        this.avatar = new ProductAvatarResult()
                .setId(avatar.getId())
                .setFileFolder(avatar.getFileFolder())
                .setCloudId(avatar.getCloudId())
                .setFileUrl(avatar.getFileUrl())
                .setFileName(avatar.getFileName())
                ;
    }
}
