package com.cg.orderDetail.dto;

import com.cg.product.dto.ProductResult;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderDetailDTO {
    private Long orderDetailId;
    private ProductResult product;
    private String quantity;
    private BigDecimal price;
    private BigDecimal amount;
    private String note;
}
