package com.cg.category;


import com.cg.category.dto.*;

import java.util.List;

public interface ICategoryService  {

        CategoryResult createCategory(CreationCategoryParam creationCategoryParam);

        CategoryResult updateCategory(String categoryStrId, UpdateCategoryParam updateCategoryParam);

        void deleteByIdTrue(Long id);

        CategoryResult findByIdAndDeletedFalse(Long id);

        List<CategoryResult> findAll();
}
