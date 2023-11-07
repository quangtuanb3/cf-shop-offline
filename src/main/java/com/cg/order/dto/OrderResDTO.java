package com.cg.order.dto;
import com.cg.orderDetail.dto.OrderDetailResult;
import com.cg.staff.dto.StaffResult;
import com.cg.tableOrder.dto.TableOrderResult;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class OrderResDTO {
    private Long id;
    private StaffResult staff;
    private TableOrderResult tableOrder;
    private List<OrderDetailResult> orderDetails;
    private Boolean paid;

}
