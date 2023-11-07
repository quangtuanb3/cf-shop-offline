package com.cg.product;

import com.cg.category.CategoryRepository;
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

@Service
@Transactional
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;

    private final ProductAvatarRepository productAvatarRepository;

    private final IUploadService uploadService;

    private final UploadUtils uploadUtils;

    private final ValidateUtils validateUtils;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }



    @Override
    public ProductResult createProduct(CreationProductParam creationProductParam) {
        if (!validateUtils.isNumberValid(creationProductParam.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }

        Long idCategory = Long.parseLong(creationProductParam.getCategoryId());
        Category category = categoryRepository.findByIdAndDeletedFalse(idCategory)
                .orElseThrow(() -> {
                    throw new DataInputException("Mã danh mục không tồn tại");
                });
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductImage(creationProductParam, productAvatar);

        Product product = creationProductParam.toProduct(category);

        product.setProductAvatar(productAvatar);
        productRepository.save(product);

        return  productMapper.toDTO(product);
    }

    @Override
    public List<ProductResult> findAllProductDTO() {
        List<Product> entities = productRepository.findAllByDeletedIsFalse();
        return productMapper.toDTOList(entities);
    }

    @Override
    public ProductResult update(String productIdStr, UpdateProductParam updateProductParam) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }

        Long productId = Long.parseLong(productIdStr);
        Product productDB = productRepository.findByIdAndDeletedFalse(productId).orElseThrow(()->{
        throw new DataInputException("ma san pham k ton tai");
    });




        if (!validateUtils.isNumberValid(updateProductParam.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }

        Long idCategory = Long.parseLong(updateProductParam.getCategoryId());
        Category category = categoryRepository.findByIdAndDeletedFalse(idCategory)
                .orElseThrow(()-> new ResourceNotFoundException("Not found"));

        if (updateProductParam.getAvatar() == null) {
            Product product = updateProductParam.toProductChangeImage(category);
            product.setId(productDB.getId());
            product.setProductAvatar(productDB.getProductAvatar());
            productRepository.save(product);

        }
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductImage(updateProductParam.toDTO(), productAvatar);

        Product productUpdate = updateProductParam.toProductChangeImage(category);
        productUpdate.setId(productId);
        productUpdate.setProductAvatar(productAvatar);
        productRepository.save(productUpdate);
        return productMapper.toDTO(productUpdate);
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
    public Product save(Product product) {
        return null;
    }

    @Override
    public ProductResult findByIdAndDeletedFalse(String productIdStr) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        Long productId = Long.valueOf(productIdStr);
        Product entity = productRepository.findByIdAndDeletedFalse(productId).orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        return productMapper.toDTO(entity);
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
