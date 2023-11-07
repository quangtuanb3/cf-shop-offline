package com.cg.order;

import com.cg.model.Order;
import com.cg.order.dto.OrderCreResDTO;
import com.cg.order.dto.OrderDTO;
import com.cg.order.dto.OrderResDTO;
import com.cg.order.dto.OrderUpResDTO;
import com.cg.orderDetail.dto.OrderDetailDTO;
import com.cg.staff.StaffMapper;
import com.cg.tableOrder.TableOrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final StaffMapper staffMapper;
    private final TableOrderMapper tableOrderMapper;

    public OrderResDTO toOrderResDTO(Order entity) {
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();
        for (int i = 0; i < entity.getOrderDetails().size(); i++) {
            OrderDetailDTO orderDetailDTO = entity.getOrderDetails().get(i).toOrderDetailDTO();
            orderDetailDTOS.add(orderDetailDTO);
        }
        return new OrderResDTO()
                .setId(entity.getId())
                .setStaff(staffMapper.toDTO(entity.getStaff()))
                .setTableOrder(tableOrderMapper.toDTO(entity.getTableOrder()))
                .setOrderDetails(orderDetailDTOS)
                .setPaid(entity.getPaid())
                ;
    }

    public OrderDTO toOrderDTO(Order entity) {
        return new OrderDTO()
                .setId(entity.getId())
                .setTotalAmount(entity.getTotalAmount())
                ;
    }

    public OrderCreResDTO toOrderCreResDTO(Order entity) {
        return new OrderCreResDTO()
                .setTableId(entity.getTableOrder().getId())
                ;
    }

    public OrderUpResDTO toOrderUpResDTO(Order entity) {
        return new OrderUpResDTO()
                .setTableId(entity.getTableOrder().getId())
                ;
    }
}
