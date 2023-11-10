package com.cg.product;

import com.cg.model.BaseEntity;
import com.cg.model.Product;
import com.cg.product.dto.ProductParam;
import com.cg.product.dto.ProductResult;
import org.springframework.stereotype.Component;
import vn.rananu.shared.mapper.BaseMapper;

@Component
public class ProductMapper extends BaseMapper<ProductResult, Product, ProductParam> {
}
