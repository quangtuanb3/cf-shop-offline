package com.cg.product;

import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.product.dto.CreationProductParam;
import com.cg.product.dto.ProductResult;
import com.cg.product.dto.UpdateProductParam;
import com.cg.service.IGeneralService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProductService {
    ProductResult createProduct(CreationProductParam creationProductParam, Category category);

    List<ProductResult> findAllProductDTO();

    Product findById(Long id);

    ProductResult update(Long productId, UpdateProductParam updateProductParam, Category category);

    void deleteByIdTrue(Product product);

    List<ProductResult> findAllByCategoryLike(Long categoryId);

    List<ProductResult> findProductByName(String keySearch);

    List<ProductResult> findAllByCategoryLikeAndAndTitleLike(Long categoryId, String keySearch);

    Page<ProductResult> findAllProductDTOPage(Pageable pageable);

    Product findByIdAndDeletedFalse(Long id);

    Product save(Product product);
}
