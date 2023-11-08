package com.cg.product;

import com.cg.model.Product;
import com.cg.modelMapper.BaseMapper;
import com.cg.product.dto.CreationProductParam;
import org.springframework.stereotype.Component;

@Component
public class CreationProductParamMapper extends BaseMapper<CreationProductParam, Product> {
}
