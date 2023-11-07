package com.cg.category;


import com.cg.category.dto.*;

import java.util.List;

public interface ICategoryService  {

        CategoryResult createCategory(CreationCategoryParam creationCategoryParam);



        CategoryResult updateCategory(String categoryStrId, UpdateCategoryParam updateCategoryParam);

        void deleteByIdTrue(String strID);

        CategoryResult findByIdAndDeletedFalse(String id);

        List<CategoryResult> findAll();
}
