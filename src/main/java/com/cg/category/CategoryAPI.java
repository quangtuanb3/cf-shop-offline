package com.cg.category;


import com.cg.category.dto.*;
import com.cg.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryAPI {


    private final ICategoryService categoryService;





    private final AppUtils appUtils;

    @GetMapping
    public ResponseEntity<?> showCategory(){


        return new ResponseEntity<>(categoryService.findAll(),HttpStatus.OK);
    }
    @GetMapping("/{categoryId}")
    public ResponseEntity<?> getById(@PathVariable("categoryId") Long id){
        return new ResponseEntity<>(categoryService.findByIdAndDeletedFalse(id),HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> createCategory(@RequestBody CreationCategoryParam creationCategoryParam, BindingResult bindingResult) {

        new CreationCategoryParam().validate(creationCategoryParam,bindingResult);
        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }
        return new ResponseEntity<>(categoryService.createCategory(creationCategoryParam), HttpStatus.CREATED);
    }

    @PatchMapping("/edit/{categoryId}")
    public ResponseEntity<?> updateCategory(@PathVariable("categoryId") String categoryIdStr, @RequestBody UpdateCategoryParam updateCategoryParam, BindingResult bindingResult){

         new UpdateCategoryParam().validate(updateCategoryParam,bindingResult);
        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }
        return new ResponseEntity<>(categoryService.updateCategory(categoryIdStr, updateCategoryParam),HttpStatus.OK);
    }

    @PatchMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable("categoryId") Long id){


        categoryService.deleteByIdTrue(id);
        return new ResponseEntity<>(HttpStatus.OK);

    }
}
