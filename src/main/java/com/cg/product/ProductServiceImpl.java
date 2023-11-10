package com.cg.product;

import com.cg.category.ICategoryService;
import com.cg.model.Product;
import com.cg.model.ProductAvatar;
import com.cg.product.dto.ProductParam;
import com.cg.product.dto.ProductResult;
import com.cg.productAvatar.ProductAvatarRepository;
import com.cg.service.upload.IUploadService;
import com.cg.utils.UploadUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import vn.rananu.shared.exception.NotFoundException;
import vn.rananu.shared.exception.OperationException;

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
    private final ProductMapper productMapper;
    private final ICategoryService categoryService;


    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("product.exception.notFound"));
    }

    @Override
    @Transactional
    public ProductResult createProduct(ProductParam productParam) {
        categoryService.findByIdAndDeletedFalse(Long.valueOf(productParam.getCategoryId()));
        ProductAvatar productAvatar = new ProductAvatar();
//        productAvatarRepository.save(productAvatar);
        uploadAndSaveProductImage(productParam.getAvatar(), productAvatar);
        Product entity = productMapper.toEntity(productParam);
        entity.setProductAvatar(productAvatar);
//        productRepository.save(entity);
        return productMapper.toDTO(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> findAllProductResult() {
        return productMapper.toDTOList(productRepository.findAllByDeletedIsFalse());
    }

    @Override
    @Transactional
    public ProductResult update(Long productId, ProductParam updateProductParam) {
        Product entity = productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("product.exception.notFound"));
        categoryService.findByIdAndDeletedFalse(Long.valueOf(updateProductParam.getCategoryId()));
        if (updateProductParam.getAvatar() == null) {
            productMapper.transferFields(updateProductParam, entity, true);
            return productMapper.toDTO(entity);
        }
        ProductAvatar productAvatar = new ProductAvatar();
        uploadAndSaveProductImage(updateProductParam.getAvatar(), productAvatar);
        productMapper.transferFields(updateProductParam, entity);
        entity.setProductAvatar(productAvatar);
        return productMapper.toDTO(entity);
    }


    @Transactional
    public void deleteById(Long id) {
        this.findByIdAndDeletedFalse(id).setDeleted(true);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> findAllByCategoryLike(Long categoryId) {
        return productMapper.toDTOList(productRepository.findAllByCategoryId(categoryId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findProductByName(String keySearch) {
        return productRepository.findProductByTitle(keySearch);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Product> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch) {
        return productRepository.findAllByCategoryIdAndTitleLike(categoryId, keySearch);
    }

    @Override
    public Page<ProductResult> findAllProductDTOPage(Pageable pageable) {
        Page<Product> result = productRepository.findAll(pageable);
        return result.map(productMapper::toDTO);
    }

    @Override
    @Transactional
    public Product save(Product product) {
        return productRepository.save(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Product findByIdAndDeletedFalse(Long productId) {
        return productRepository.findByIdAndDeletedFalse(productId)
                .orElseThrow(() -> new NotFoundException("product.exception.notFound"));

    }

    @Override
    @Transactional(readOnly = true)
    public ProductResult getByIdAndDeletedFalse(Long productId) {
        return productMapper.toDTO(this.findByIdAndDeletedFalse(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> getProductByName(String keySearch) {
        List<Product> entities = this.findProductByName(keySearch);
        if (entities.size() == 0) throw new NotFoundException("product.exception.notFound");
        return productMapper.toDTOList(entities);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductResult> getAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch) {
        List<Product> entities = this.findAllByCategoryLikeAndAndTitleLike(categoryId, keySearch);
        if (entities.size() == 0) throw new NotFoundException("product.exception.notFound");
        return productMapper.toDTOList(entities);
    }


    @Transactional
    public void uploadAndSaveProductImage(MultipartFile images, ProductAvatar productAvatar) {
        try {
            Map uploadResult = uploadService.uploadImage(images,
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
            throw new OperationException("product.exception.uploadFail");
        }
    }


}
