package com.cg.orderDetail;

import com.cg.model.Order;
import com.cg.model.OrderDetail;
import com.cg.model.Product;
import com.cg.model.TableOrder;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.orderDetail.dto.CreationOrderDetailParam;
import com.cg.orderDetail.dto.OrderDetailProductUpResDTO;
import com.cg.orderDetail.dto.UpdateOrderDetailParam;
import com.cg.tableOrder.TableOrderMapper;
import com.cg.tableOrder.dto.TableOrderResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderDetailMapper {
    public CreationOrderDetailParam toDTO(OrderDetail orderDetail, TableOrder tableOrder, Product product, Order order) {
        return new CreationOrderDetailParam()
        .setOrderDetailId(orderDetail.getId())
        .setTable(tableOrder)
        .setProductId(product.getId())
        .setTitle(product.getTitle())
        .setPrice(orderDetail.getPrice())
        .setQuantity(orderDetail.getQuantity())
        .setAmount(orderDetail.getAmount())
        .setNote(orderDetail.getNote())
        .setTotalAmount(order.getTotalAmount())
        .setAvatar(product.getProductAvatar().toProductAvatarDTO());
    }


    public UpdateOrderDetailParam toDTOList(List<OrderDetailProductUpResDTO> newOrderDetails, Order order,TableOrderMapper tableOrderMapper) {
        return new UpdateOrderDetailParam()
                .setTable(tableOrderMapper.toDTO(order.getTableOrder()))
                .setProducts(newOrderDetails)
                .setTotalAmount(order.getTotalAmount());
    }

    public OrderUpChangeToTableResDTO toDTOUpChang(TableOrderMapper tableOrderMapper, List<OrderDetailProductUpResDTO> newOrderDetails, Order orderBusy) {
        return new OrderUpChangeToTableResDTO()
                .setProducts(newOrderDetails)
                .setTableOrder(tableOrderMapper.toDTO(orderBusy.getTableOrder()))
                .setTotalAmount(orderBusy.getTotalAmount());
    }
}
