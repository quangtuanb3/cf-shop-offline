package com.cg.category;


import com.cg.category.dto.*;
import com.cg.model.Category;
import com.cg.service.IGeneralService;

import java.util.List;
import java.util.Optional;

public interface ICategoryService extends IGeneralService<Category,Long> {

        CategoryCreResDTO createCategory(CategoryCreReqDTO categoryCreReqDTO);

        List<CategoryDTO> findAllCategoryDTO();

        CategoryDTO updateCategory(Long categoryId, CategoryUpReqDTO categoryUpReqDTO);

        void deleteByIdTrue(Category category);

        Optional<Category> findByIdAndDeletedFalse(Long id);

}
