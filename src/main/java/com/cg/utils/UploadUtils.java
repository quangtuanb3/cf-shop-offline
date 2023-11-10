package com.cg.utils;

import com.cg.model.ProductAvatar;
import com.cg.service.upload.UploadServiceImpl;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.rananu.shared.exception.OperationException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UploadUtils {
    public static final String IMAGE_UPLOAD_FOLDER = "ledinhhau";
    private final UploadServiceImpl uploadService;


    public Map buildImageUploadParams(ProductAvatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new OperationException("product.exception.uploadFail");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }



}
