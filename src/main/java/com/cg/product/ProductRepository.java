package com.cg.product;

import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.product.dto.ProductResult;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByIdAndDeletedFalse(Long id);
    List<Product> findAllByDeletedIsFalse();

    List<Product> findAllByCategoryId(Long categoryId);

    List<Product> findProductByTitle(String keySearch);

    List<Product> findAllByCategoryIdAndTitleLike(@Param("categoryId") Long categoryId, @Param("title") String title);

    Page<Product> findAll(Pageable pageable);
}
