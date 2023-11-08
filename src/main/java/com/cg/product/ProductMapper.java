package com.cg.product;

import com.cg.model.Product;
import com.cg.product.dto.ProductResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProductMapper {
    public ProductResult toDTO(Product entity){
        return new ProductResult()
                .setId(String.valueOf(entity.getId()))
                .setTitle(entity.getTitle())
                .setPrice(entity.getPrice())
                .setUnit(entity.getUnit())
                .setCategory(entity.getCategory().toCategoryDTO())
                .setAvatar(entity.getProductAvatar().toProductAvatarDTO());
    }
    public List<ProductResult> toDTOList(List<Product> entities) {
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
