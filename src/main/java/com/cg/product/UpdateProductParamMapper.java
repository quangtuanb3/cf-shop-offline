package com.cg.product;

import com.cg.model.Product;
import com.cg.modelMapper.BaseMapper;
import com.cg.product.dto.CreationProductParam;
import com.cg.product.dto.UpdateProductParam;
import org.springframework.stereotype.Component;

@Component
public class UpdateProductParamMapper extends BaseMapper<UpdateProductParam, Product> {
}
