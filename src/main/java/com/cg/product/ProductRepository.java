package com.cg.product;

import com.cg.model.Product;
import com.cg.product.dto.ProductResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndDeletedFalse(Long id);
    List<Product> findAllByDeletedIsFalse();

    @Query("SELECT NEW com.cg.product.DTO.ProductDTO (" +
            "pr.id, " +
            "pr.title, " +
            "pr.price, " +
            "pr.unit, " +
            "pr.category, " +
            "pr. productAvatar " +
            ") " +
            "From Product AS pr " +
            "WHERE pr.category.id = :categoryId " +
            "AND pr.deleted = false ")
    List<ProductResult> findAllByCategoryLike(Long categoryId);

    @Query("SELECT NEW com.cg.product.DTO.ProductDTO (" +
            "pro.id, " +
            "pro.title, " +
            "pro.price, " +
            "pro.unit, " +
            "pro.category, " +
            "pro.productAvatar" +
            ") " +
            "FROM Product AS pro " +
            "WHERE pro.title like :keySearch " +
            "AND pro.deleted = false")
    List<ProductResult> findProductByName(String keySearch);


    @Query("SELECT NEW com.cg.product.DTO.ProductDTO (" +
            "pro.id, " +
            "pro.title, " +
            "pro.price, " +
            "pro.unit, " +
            "pro.category, " +
            "pro.productAvatar" +
            ") " +
            "FROM Product as pro " +
            "WHERE pro.title like :keySearch and pro.category.id = :categoryId " +
            "ORDER BY pro.price desc"
    )
    List<ProductResult> findAllByCategoryLikeAndAndTitleLike(@Param("categoryId") Long categoryId, @Param("keySearch") String keySearch);

    @Query("SELECT NEW com.cg.product.DTO.ProductDTO ( " +
            "pro.id, " +
            "pro.title, " +
            "pro.price, " +
            "pro.unit, " +
            "pro.category, " +
            "pro.productAvatar " +
            ") " +
            "FROM Product as pro " +
            "WHERE pro.deleted = false " +
            "ORDER BY pro.id ASC"
    )
    Page<ProductResult> findAllProductDTOPage(Pageable pageable);
}
