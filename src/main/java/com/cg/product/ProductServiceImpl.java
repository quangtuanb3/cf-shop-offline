package com.cg.product;

import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.model.ProductAvatar;
import com.cg.product.dto.CreationProductParam;
import com.cg.product.dto.ProductResult;
import com.cg.product.dto.UpdateProductParam;
import com.cg.productAvatar.ProductAvatarRepository;
import com.cg.service.upload.IUploadService;
import com.cg.utils.UploadUtils;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private ProductRepository productRepository;

    private ProductAvatarRepository productAvatarRepository;

    private IUploadService uploadService;

    private UploadUtils uploadUtils;

    private ValidateUtils validateUtils;

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }


    public Product save(Product product) {
        return productRepository.save(product);
    }

    public void delete(Product product) {

    }

    public void deleteById(Long id) {

    }

    @Override
    public ProductResult createProduct(CreationProductParam creationProductParam, Category category) {
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductImage(creationProductParam, productAvatar);

        Product product = creationProductParam.toProduct(category);

        product.setProductAvatar(productAvatar);
        productRepository.save(product);

        return new ProductResult().toDTO(product);
    }

    @Override
    public List<ProductResult> findAllProductDTO() {
        return productRepository.findAllByDeletedIsFalse().stream().map(Product::toDTO).collect(Collectors.toList());
    }

    @Override
    public ProductResult update(Long id, UpdateProductParam updateProductParam, Category category) {
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductImage(updateProductParam.toDTO(), productAvatar);

        Product productUpdate = updateProductParam.toProductChangeImage(category);
        productUpdate.setId(id);
        productUpdate.setProductAvatar(productAvatar);
        productRepository.save(productUpdate);
        return new ProductResult().toDTO(productUpdate);
    }

    @Override
    public void deleteByIdTrue(Product product) {
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public List<ProductResult> findAllByCategoryLike(Long categoryId) {
        return productRepository.findAllByCategoryLike(categoryId);
    }

    @Override
    public List<ProductResult> findProductByName(String keySearch) {
        return productRepository.findProductByName(keySearch);
    }

    @Override
    public List<ProductResult> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch) {
        return productRepository.findAllByCategoryLikeAndAndTitleLike(categoryId, keySearch);
    }

    @Override
    public Page<ProductResult> findAllProductDTOPage(Pageable pageable) {
        return productRepository.findAllProductDTOPage(pageable);
    }

    @Override
    public Product findByIdAndDeletedFalse(Long id) {
        return productRepository.findByIdAndDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Not found!"));
    }

    private void uploadAndSaveProductImage(CreationProductParam creationProductParam, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(creationProductParam.getAvatar(), uploadUtils.buildImageUploadParams(productAvatar));
            String fileUrl = (String) uploadResult.get("secure_url");
            String fileFormat = (String) uploadResult.get("format");

            productAvatar.setFileName(productAvatar.getId() + "." + fileFormat);
            productAvatar.setFileUrl(fileUrl);
            productAvatar.setFileFolder(UploadUtils.IMAGE_UPLOAD_FOLDER);
            productAvatar.setCloudId(productAvatar.getFileFolder() + "/" + productAvatar.getId());
            productAvatarRepository.save(productAvatar);

        } catch (IOException e) {
            e.printStackTrace();
            throw new DataInputException("Upload hình ảnh thất bại");
        }
    }


}
