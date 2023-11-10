package com.cg.product;

import com.cg.model.Product;
import com.cg.product.dto.ProductParam;
import com.cg.product.dto.ProductResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductService {
    ProductResult createProduct(ProductParam productParam);

    List<ProductResult> findAllProductResult();

    Product findById(Long id);

    ProductResult update(Long productId, ProductParam updateProductParam);

    void deleteById(Long id);

    List<ProductResult> findAllByCategoryLike(Long categoryId);

    List<Product> findProductByName(String keySearch);

    List<Product> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch);

    Page<ProductResult> findAllProductDTOPage(Pageable pageable);

    Product save(Product product);

    Product findByIdAndDeletedFalse(Long productId);

    @Transactional(readOnly = true)
    ProductResult getByIdAndDeletedFalse(Long productId);

    List<ProductResult> getProductByName(String keySearch);

    List<ProductResult> getAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch);
}
