package com.cg.model;


import com.cg.category.dto.CategoryResult;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="categories")
@Accessors(chain = true)
public class Category extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;


    @OneToMany(mappedBy = "category")
    private List<Product> products;

    public Category(Long id) {
        this.id=id;
    }

    public CategoryResult toCategoryDTO() {
        return new CategoryResult()
                .setId(id)
                .setTitle(title);
    }
}
