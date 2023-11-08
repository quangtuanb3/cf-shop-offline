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
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements IProductService {

    private final ProductRepository productRepository;
    private final ProductAvatarRepository productAvatarRepository;
    private final IUploadService uploadService;
    private final UploadUtils uploadUtils;
    private final ValidateUtils validateUtils;
    private final ProductResultMapper productResultMapper;
    private final CategoryRepository categoryRepository;
    private final CreationProductParamMapper creationProductParamMapper;
    private final UpdateProductParamMapper updateProductParamMapper;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    @Transactional
    public ProductResult createProduct(CreationProductParam creationProductParam) {
        if (!validateUtils.isNumberValid(creationProductParam.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }

        Long idCategory = Long.parseLong(creationProductParam.getCategoryId());

        categoryRepository.findByIdAndDeletedFalse(idCategory)
                .orElseThrow(() -> new DataInputException("Mã danh mục không tồn tại"));

        ProductAvatar productAvatar = new ProductAvatar();

        productAvatarRepository.save(productAvatar);

        uploadAndSaveProductImage(creationProductParam, productAvatar);

        Product product = creationProductParamMapper.toEntity(creationProductParam);
        product.setProductAvatar(productAvatar);
        productRepository.save(product);
        return productResultMapper.toDTO(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> findAllProductResult() {
        List<Product> entities = productRepository.findAllByDeletedIsFalse();
        return productResultMapper.toDTOList(entities);
    }

    @Override
    @Transactional
    public ProductResult update(String productIdStr, UpdateProductParam updateProductParam) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }

        Long productId = Long.parseLong(productIdStr);
        Product productDB = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new DataInputException("ma san pham k ton tai"));

        if (!validateUtils.isNumberValid(updateProductParam.getCategoryId())) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }

        Long idCategory = Long.parseLong(updateProductParam.getCategoryId());
        Category category = categoryRepository.findByIdAndDeletedFalse(idCategory)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));

        if (updateProductParam.getAvatar() == null) {
            Product product = updateProductParamMapper.toEntity(updateProductParam);
            product.setId(productDB.getId());
            product.setProductAvatar(productDB.getProductAvatar());
            productRepository.save(product);

        }
        ProductAvatar productAvatar = new ProductAvatar();
        productAvatarRepository.save(productAvatar);
        Product productUpdate = updateProductParamMapper.toEntity(updateProductParam);

        uploadAndSaveProductImage(new CreationProductParam()
                .setAvatar(updateProductParam.getAvatar()), productAvatar);

        productUpdate.setId(productId);
        productUpdate.setProductAvatar(productAvatar);
        productRepository.save(productUpdate);
        return productResultMapper.toDTO(productUpdate);
    }


    @Transactional
    public void deleteById(Long id) {
        findById(id).setDeleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> findAllByCategoryLike(Long categoryId) {
        return productResultMapper.toDTOList(productRepository.findAllByCategoryId(categoryId));
    }

    @Override
    public List<ProductResult> findProductByName(String keySearch) {
        return productResultMapper.toDTOList(productRepository.findProductByTitle(keySearch));
    }

    @Override
    public List<ProductResult> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch) {
        return productResultMapper.toDTOList(productRepository.findAllByCategoryIdAndTitleLike(categoryId, keySearch));
    }

    @Override
    public Page<ProductResult> findAllProductDTOPage(Pageable pageable) {
        Page<Product> result = productRepository.findAll(pageable);
        return result.map(productResultMapper::toDTO);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductResult findByIdAndDeletedFalse(String productIdStr) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        Long productId = Long.valueOf(productIdStr);
        Product entity = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));
        return productResultMapper.toDTO(entity);
    }

    @Transactional
    public void uploadAndSaveProductImage(CreationProductParam creationProductParam, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(creationProductParam.getAvatar(),
                    uploadUtils.buildImageUploadParams(productAvatar));

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
