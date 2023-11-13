package com.cg.product;

import com.cg.category.ICategoryService;
import com.cg.model.Product;
import com.cg.model.ProductAvatar;
import com.cg.product.ProductRepository;
import com.cg.product.dto.ProductParam;
import com.cg.product.dto.ProductResult;
import com.cg.productAvatar.ProductAvatarRepository;
import com.cg.service.upload.IUploadService;
import com.cg.utils.UploadUtils;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import vn.rananu.shared.exception.NotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductAvatarRepository productAvatarRepository;

    @Mock
    private IUploadService uploadService;

    @Mock
    private UploadUtils uploadUtils;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void testFindAll() {
        // Mock the behavior of the productRepository
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product(), new Product()));

        // Call the method to be tested
        List<Product> result = productService.findAll();

        // Assertions
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        // Mock the behavior of the productRepository
        when(productRepository.findById(any(Long.class))).thenReturn(java.util.Optional.of(new Product()));

        // Call the method to be tested
        Product result = productService.findById(1L);

        // Assertions
        assertNotNull(result);
    }

    // Similar tests can be written for other methods in your service class

    @Test
    void testUploadAndSaveProductImage() throws IOException {
        // Mock the behavior of the uploadService and productAvatarRepository
        MultipartFile mockMultipartFile = Mockito.mock(MultipartFile.class);
        ProductAvatar productAvatar = new ProductAvatar();
        when(uploadService.uploadImage(any(MultipartFile.class), any())).thenReturn(Mockito.mock(Map.class));

        // Call the method to be tested
        productService.uploadAndSaveProductImage(mockMultipartFile, productAvatar);

        // Assertions or verifications can be added as needed
        assertNotNull(productAvatar.getFileUrl());
        assertNotNull(productAvatar.getFileName());
    }
}
