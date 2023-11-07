package com.cg.category;


import com.cg.category.dto.*;
import com.cg.exception.DataInputException;
import com.cg.model.Category;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
public class CategoryAPI {

    @Autowired
    private ICategoryService categoryService;

    @Autowired
    private ValidateUtils validateUtils;

    @Autowired
    private AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> showCategory(){


        return new ResponseEntity<>(categoryService.findAllCategoryDTO(),HttpStatus.OK);
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getById(@PathVariable("categoryId") String categoryIdStr){

        if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);

        Category category = categoryService.findByIdAndDeletedFalse(categoryId).orElseThrow(() -> {
            throw new DataInputException("Mã danh mục không tồn tại");
        });

        CategoryDTO categoryDTO = category.toCategoryDTO();
        return new ResponseEntity<>(categoryDTO,HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CategoryCreReqDTO categoryCreReqDTO, BindingResult bindingResult) {

        new CategoryCreReqDTO().validate(categoryCreReqDTO,bindingResult);
        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }
        return new ResponseEntity<>(categoryService.createCategory(categoryCreReqDTO), HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") String categoryIdStr, @RequestBody CategoryUpReqDTO categoryUpReqDTO, BindingResult bindingResult){


        if (!validateUtils.isNumberValid(categoryIdStr)){
            throw new DataInputException("Mã không hợp lệ");
        }

        Long idCategory =Long.parseLong(categoryIdStr);
        categoryService.findByIdAndDeletedFalse(idCategory).orElseThrow(() ->{
            throw new DataInputException("Mã không tồn tại");
        });
         new CategoryUpReqDTO().validate(categoryUpReqDTO,bindingResult);
        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

//        CategoryUpResDTO categoryUpdate = categoryService.updateCategory(idCategory,categoryUpReqDTO);

        return new ResponseEntity<>(categoryService.updateCategory(idCategory,categoryUpReqDTO),HttpStatus.OK);
    }

    @PatchMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") String categoryIdStr){

    if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);
        Category category = categoryService.findByIdAndDeletedFalse(categoryId).orElseThrow(() -> {
           throw new DataInputException("Mã không tồn tại ");
        });
        categoryService.deleteByIdTrue(category);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
