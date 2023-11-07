package com.cg.order.dto;
import com.cg.orderDetail.dto.CreationOrderDetailParam;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class OrderUpResDTO {
    private Long tableId;
    private BigDecimal totalAmount;

    private List<CreationOrderDetailParam> orderDetails;
}
