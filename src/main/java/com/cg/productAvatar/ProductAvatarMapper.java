package com.cg.productAvatar;

import com.cg.model.ProductAvatar;
import com.cg.productAvatar.dto.ProductAvatarResult;
import org.springframework.stereotype.Component;

@Component
public class ProductAvatarMapper {
    public ProductAvatarResult toDTO(ProductAvatar entity) {
        return new ProductAvatarResult()
                .setId(entity.getId())
                .setFileName(entity.getFileName())
                .setFileFolder(entity.getFileFolder())
                .setFileUrl(entity.getFileUrl())
                .setCloudId(entity.getCloudId());
    }

    public ProductAvatar toEntity(ProductAvatarResult dto) {
        return new ProductAvatar()
                .setId(dto.getId())
                .setFileName(dto.getFileName())
                .setFileFolder(dto.getFileFolder())
                .setFileUrl(dto.getFileUrl())
                .setCloudId(dto.getCloudId());
    }


}
