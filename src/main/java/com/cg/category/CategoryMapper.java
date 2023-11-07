package com.cg.category;

import com.cg.category.dto.CreationCategoryParam;
import com.cg.category.dto.CategoryResult;
import com.cg.model.Category;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CategoryMapper  {
    public CategoryResult toDTO(Category entity){
        return new CategoryResult()
                .setId(entity.getId())
                .setTitle(entity.getTitle());
    }

    public Category toEntity(CreationCategoryParam creationCategoryParam) {
        return new Category()
                .setId(creationCategoryParam.toDTO().getId())
                .setTitle(creationCategoryParam.getTitle());
    }

    public List<CategoryResult> toDTOList(List<Category> entities){
        return entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}
