package com.cg.category;


import com.cg.category.dto.*;
import com.cg.exception.DataInputException;
import com.cg.model.Category;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
            throw new DataInputException("Mã không hợp lệ");
        }

        Long idCategory =Long.parseLong(categoryStrId);
        categoryRepository.findByIdAndDeletedFalse(idCategory).orElseThrow(() ->{
            throw new DataInputException("Mã không tồn tại");
        });
        Category category = updateCategoryParam.toCategoryUpreqDTO(idCategory);
        categoryRepository.save(category);
        return categoryMapper.toDTO(category);
    }


    @Override
    @Transactional
    public void deleteByIdTrue(String strID) {
        if (!validateUtils.isNumberValid(strID)) {
            throw new DataInputException("Mã không hợp lệ");
        }
        Long categoryId = Long.parseLong(strID);
        Category entity = categoryRepository.findByIdAndDeletedFalse(categoryId).orElseThrow(() -> {
            throw new DataInputException("Mã không tồn tại ");
        });
        entity.setDeleted(true);
        categoryRepository.save(entity);
    }

    @Override
    @Transactional
    public CategoryResult findByIdAndDeletedFalse(String categoryIdStr) {
        if (!validateUtils.isNumberValid(categoryIdStr)) {
            throw new DataInputException("Mã danh mục không hợp lệ");
        }
        Long categoryId = Long.parseLong(categoryIdStr);
        Category entity = categoryRepository.findByIdAndDeletedFalse(categoryId).orElseThrow(()->{
            throw new DataInputException("Mã danh mục không tồn tại");
        });
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
