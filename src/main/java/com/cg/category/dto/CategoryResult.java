package com.cg.category.dto;

import com.cg.model.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CategoryResult {
    private Long id;
    private String title;

    public Category toDTO() {
        return new Category()
                .setId(id)
                .setTitle(title);
    }
}
