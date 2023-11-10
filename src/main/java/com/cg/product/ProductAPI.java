package com.cg.product;

import com.cg.category.CategoryServiceImpl;
import com.cg.product.dto.*;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.grammars.hql.HqlParser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.rananu.shared.annotation.RananuBody;

import java.util.List;

//@RestController
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/products")
public class ProductAPI {
    private final IProductService productService;
    private final CategoryServiceImpl categoryService;
    private final ValidateUtils validateUtils;
    private final AppUtils appUtils;

    @GetMapping
    @RananuBody
    public List<?> showProducts() {
        return productService.findAllProductResult();

    }

    @GetMapping("/{productId}")
    @RananuBody
    public ProductResult getByIdProduct(@PathVariable("productId") Long productId) {
        return productService.getByIdAndDeletedFalse(productId);
    }

    @PostMapping("/create")
    @RananuBody(message = "validate.user.create.success")
    public ProductResult createProduct(@Valid @ModelAttribute ProductParam productParam) {
        return productService.createProduct(productParam);
    }

    @PatchMapping("/edit/{productId}")
    @RananuBody(message = "validate.user.update.success")
    public ProductResult updateProduct(@PathVariable("productId") Long productId,
                                       @ModelAttribute ProductParam updateProductParam) {
        return productService.update(productId, updateProductParam);
    }

    @DeleteMapping("/{productId}")
    @RananuBody
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") Long productId) {
        productService.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/search/{categoryId}")
    @RananuBody
    public List<ProductResult> getProductByCategory(@PathVariable("categoryId") Long categoryId) {
        return productService.findAllByCategoryLike(categoryId);
    }

    @GetMapping("/searchName/{keySearch}")
    @RananuBody
    public List<ProductResult> getProductByName(@PathVariable("keySearch") String keySearch) {
        keySearch = '%' + keySearch + '%';
        return productService.getProductByName(keySearch);
    }

    @GetMapping("{/categoryId}/{keySearch}")
    @RananuBody
    public List<ProductResult> searchProductCategory(@PathVariable("keySearch") String keySearch, @PathVariable("categoryId") Long categoryId) {
        categoryService.findByIdAndDeletedFalse(categoryId);
        keySearch = '%' + keySearch + '%';
        return productService.getAllByCategoryLikeAndAndTitleLike(categoryId, keySearch);
    }

    @GetMapping("/page")
    @RananuBody
    public Page<ProductResult> getAllProductDTO(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        return productService.findAllProductDTOPage(PageRequest.of(page - 1, pageSize));
    }
}
