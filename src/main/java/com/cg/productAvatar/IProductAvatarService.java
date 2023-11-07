package com.cg.productAvatar;

import com.cg.model.ProductAvatar;
import com.cg.service.IGeneralService;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface IProductAvatarService {
    @Transactional(readOnly = true)
    List<ProductAvatar> findAll();

    ProductAvatar findById(Long id);

    ProductAvatar save(ProductAvatar productAvatar);

    void delete(ProductAvatar productAvatar);

    void deleteById(Long id);
}
