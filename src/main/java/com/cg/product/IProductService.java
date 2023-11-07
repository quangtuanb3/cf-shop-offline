package com.cg.product;

import com.cg.model.Product;
import com.cg.product.dto.CreationProductParam;
import com.cg.product.dto.ProductResult;
import com.cg.product.dto.UpdateProductParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IProductService {
    ProductResult createProduct(CreationProductParam creationProductParam);

    List<ProductResult> findAllProductDTO();

    Product findById(Long id);

    ProductResult update(String productId, UpdateProductParam updateProductParam);

    void deleteByIdTrue(Product product);

    List<ProductResult> findAllByCategoryLike(Long categoryId);

    List<ProductResult> findProductByName(String keySearch);

    List<ProductResult> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch);

    Page<ProductResult> findAllProductDTOPage(Pageable pageable);

    Product save(Product product);

    ProductResult findByIdAndDeletedFalse(String productIdStr);
}
