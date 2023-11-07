package com.cg.category.dto;

import com.cg.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class UpdateCategoryParam implements Validator {

    private String title;

    @Override
    public boolean supports(Class<?> clazz) {
           return UpdateCategoryParam.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateCategoryParam updateCategoryParam = (UpdateCategoryParam) target;
        String title = updateCategoryParam.title;
        if (title.isEmpty()) {
            errors.rejectValue("title", "title.null", "Tên không được phép rỗng");
        }


    }

    public Category toCategoryUpreqDTO(Long categoryId) {
        return new Category()
                .setId(categoryId)
                .setTitle(title);
    }
}
