package com.cg.utils;

import com.cg.exception.DataInputException;
import com.cg.model.Product;
import com.cg.model.ProductAvatar;
import com.cg.model.StaffAvatar;
import com.cg.service.upload.UploadServiceImpl;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class UploadUtils {
    public static final String IMAGE_UPLOAD_FOLDER = "ledinhhau";
    private final UploadServiceImpl uploadService;


    public Map buildImageUploadParams(ProductAvatar productAvatar) {
        if (productAvatar == null || productAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của sản phẩm chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, productAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }


    public Map buildImageUploadParamsStaff(StaffAvatar staffAvatar) {
        if (staffAvatar == null || staffAvatar.getId() == null)
            throw new DataInputException("Không thể upload hình ảnh của nhân viên chưa được lưu");

        String publicId = String.format("%s/%s", IMAGE_UPLOAD_FOLDER, staffAvatar.getId());

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }


    public Map buildImageDestroyParams(Product product, String publicId) {
        if (product == null || product.getId() == null)
            throw new DataInputException("Không thể destroy hình ảnh của sản phẩm không xác định");

        return ObjectUtils.asMap(
                "public_id", publicId,
                "overwrite", true,
                "resource_type", "image"
        );
    }

    public StaffAvatar uploadAndStaffImage(StaffAvatar staffAvatar, MultipartFile file) {
        try {
            Map uploadResult = uploadService.uploadImage(file, buildImageUploadParamsStaff(staffAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            staffAvatar.setFileName(staffAvatar.getId() + "." + fileFormat);
            staffAvatar.setFileUrl(fileUrl);
            staffAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            staffAvatar.setCloudId(staffAvatar.getFileFolder() + "/" + staffAvatar.getId());
            return staffAvatar;

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }

}
