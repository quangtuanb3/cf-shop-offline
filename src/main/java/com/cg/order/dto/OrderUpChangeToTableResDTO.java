package com.cg.order.dto;

import com.cg.orderDetail.dto.OrderDetailProductUpResDTO;
import com.cg.tableOrder.dto.TableOrderResult;
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
public class OrderUpChangeToTableResDTO {
    private TableOrderResult tableOrder;
    private BigDecimal totalAmount;
    private List<OrderDetailProductUpResDTO> products;

}
