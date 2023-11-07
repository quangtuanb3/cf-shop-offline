package com.cg.category.dto;

import com.cg.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CategoryCreReqDTO implements Validator {

    private String title;

    public Category toDTO(){
        return new Category()
                .setId(null)
                .setTitle(title)
                ;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return CategoryCreReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        CategoryCreReqDTO categoryCreReqDTO = (CategoryCreReqDTO) target;

        String title = categoryCreReqDTO.title;

        if (title.isEmpty()) {
            errors.rejectValue("title","title.null","Tên không được phép rỗng");

        }


    }
}
