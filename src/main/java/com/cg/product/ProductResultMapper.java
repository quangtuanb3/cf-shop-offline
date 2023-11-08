package com.cg.product;

import com.cg.model.Product;
import com.cg.modelMapper.BaseMapper;
import com.cg.product.dto.ProductResult;
import org.springframework.stereotype.Component;

@Component
public class ProductResultMapper extends BaseMapper<ProductResult, Product> {
}
