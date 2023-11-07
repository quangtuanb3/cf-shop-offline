package com.cg.category;

import com.cg.category.dto.CategoryCreReqDTO;
import com.cg.category.dto.CategoryDTO;
import com.cg.model.Category;
import com.cg.modelMapper.BaseMapper;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper  {
    public CategoryDTO toDTO(Long id, String title){
        return new CategoryDTO()
                .setId(id)
                .setTitle(title);
    }

    public Category toEntity(CategoryCreReqDTO categoryCreReqDTO) {
    }
}
