package com.cg.product;

import com.cg.category.CategoryServiceImpl;
import com.cg.exception.DataInputException;
import com.cg.exception.ResourceNotFoundException;
import com.cg.model.Category;
import com.cg.model.Product;
import com.cg.product.dto.*;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductAPI {
    private final IProductService productService;
    private final CategoryServiceImpl categoryService;
    private final ValidateUtils validateUtils;
    private final AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> showProducts() {
        return new ResponseEntity<>(productService.findAllProductResult(), HttpStatus.OK);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> findByIdProduct(@PathVariable("productId") String productIdStr) {
        return new ResponseEntity<>(productService.findByIdAndDeletedFalse(productIdStr), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@ModelAttribute CreationProductParam creationProductParam, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        return new ResponseEntity<>(productService.createProduct(creationProductParam), HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{productId}")
    public ResponseEntity<?> updateProduct(@PathVariable("productId") String productIdStr, @ModelAttribute UpdateProductParam updateProductParam, BindingResult bindingResult) {
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        ProductResult dto = productService.update(productIdStr, updateProductParam);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable("productId") String productIdStr) {
        if (!validateUtils.isNumberValid(productIdStr)) {
            throw new DataInputException("Mã sản phẩm không hợp lệ");
        }
        productService.findByIdAndDeletedFalse(productIdStr);
        Long productId = Long.parseLong(productIdStr);

        productService.deleteById(productId);
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @GetMapping("/search/{categoryId}")
    public ResponseEntity<List<ProductResult>> getProductBycategory(@PathVariable("categoryId") String categoryIdStr) {

        if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);

        if (!categoryService.existById(categoryId)) {
            throw new RuntimeException("Category not found!");
        }

        List<ProductResult> productResult = productService.findAllByCategoryLike(categoryId);

        return new ResponseEntity<>(productResult, HttpStatus.OK);
    }

    @GetMapping("/searchName/{keySearch}")
    public ResponseEntity<List<ProductResult>> getProductByName(@PathVariable("keySearch") String keySearch) {
        keySearch = '%' + keySearch + '%';

        List<ProductResult> productResult = productService.findProductByName(keySearch);
        if (productResult.isEmpty()) {
            throw new DataInputException("Sản phẩm này không tồn tại");
        }
        return new ResponseEntity<>(productResult, HttpStatus.OK);
    }

    @GetMapping("{/categoryId}/{keySearch}")
    public ResponseEntity<List<ProductResult>> searchProductCategory(@PathVariable("keySearch") String keySearch, @PathVariable("categoryId") String categoryIdStr) {
        if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);
        categoryService.findByIdAndDeletedFalse(categoryIdStr);
        keySearch = '%' + keySearch + '%';
        List<ProductResult> productResult = productService.findAllByCategoryLikeAndAndTitleLike(categoryId, keySearch);
        if (productResult.isEmpty()) {
            throw new DataInputException("Sản phẩm này không tồn tại");
        }
        return new ResponseEntity<>(productResult, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<ProductResult>> getAllProductDTO(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        try {
            Page<ProductResult> productDTOS = productService.findAllProductDTOPage(PageRequest.of(page - 1, pageSize));

            if (productDTOS.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(productDTOS, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
