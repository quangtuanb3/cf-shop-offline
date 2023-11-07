package com.cg.productAvatar;

import com.cg.exception.ResourceNotFoundException;
import com.cg.model.ProductAvatar;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductAvatarServiceImpl implements IProductAvatarService {

    private final ProductAvatarRepository productAvatarRepository;
    @Override
    @Transactional(readOnly = true)
    public List<ProductAvatar> findAll() {
        return productAvatarRepository.findAll();
    }

    @Override
    public ProductAvatar findById(Long id) {
        return productAvatarRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Not found!"));
    }

    @Override
    public ProductAvatar save(ProductAvatar productAvatar) {
        return productAvatarRepository.save(productAvatar);
    }

    @Override
    public void delete(ProductAvatar productAvatar) {
        productAvatarRepository.delete(productAvatar);
    }

    @Override
    public void deleteById(Long id) {
        productAvatarRepository.deleteById(id);
    }
}
