package com.cg.category;


import com.cg.category.dto.*;
import com.cg.model.Category;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.rananu.shared.exception.NotFoundException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;
    private final ValidateUtils validateUtils;

    @Override
    @Transactional
    public CategoryResult createCategory(CreationCategoryParam creationCategoryParam) {

        Category category = categoryMapper.toEntity(creationCategoryParam);
        categoryRepository.save(category);

        return categoryMapper.toDTO(category);
    }

    @Override
    @Transactional
    public CategoryResult updateCategory(String categoryStrId, UpdateCategoryParam updateCategoryParam) {
        if (!validateUtils.isNumberValid(categoryStrId)){
            throw new NotFoundException("product.exception.invalidCategory");
        }

        Long idCategory =Long.parseLong(categoryStrId);
        categoryRepository.findByIdAndDeletedFalse(idCategory)
                .orElseThrow(() -> new NotFoundException("product.exception.invalidCategory"));
        Category category = updateCategoryParam.toCategoryUpreqDTO(idCategory);
        categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }


    @Override
    @Transactional
    public void deleteByIdTrue(Long id) {
        Category entity = categoryRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException("product.exception.invalidCategory"));
        entity.setDeleted(true);
        categoryRepository.save(entity);
    }

    @Override
    @Transactional
    public CategoryResult findByIdAndDeletedFalse(Long categoryId) {
        Category entity = categoryRepository.findByIdAndDeletedFalse(categoryId)
                .orElseThrow(()-> new NotFoundException("product.exception.invalidCategory"));
        return categoryMapper.toDTO(entity);
    }

    public boolean existById(Long id) {
        return categoryRepository.existsById(id);
    }


    @Override
    @Transactional
    public List<CategoryResult> findAll() {
        List<Category>  entities = categoryRepository.findAll();
        return categoryMapper.toDTOList(entities);
    }


}
