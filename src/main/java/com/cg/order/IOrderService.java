package com.cg.order;


import com.cg.model.user.User;
import com.cg.order.dto.CreationOrderParam;
import com.cg.order.dto.OrderUpChangeToTableReqDTO;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.order.dto.OrderUpReqDTO;
import com.cg.model.*;
import com.cg.orderDetail.dto.CreationOrderDetailParam;
import com.cg.orderDetail.dto.UpdateOrderDetailParam;

import java.math.BigDecimal;
import java.util.List;

public interface IOrderService  {
    Order findByTableId(Long tableId);

    List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid);


    CreationOrderDetailParam creOrder(CreationOrderParam creationOrderParam, TableOrder tableOrder, User user);

    UpdateOrderDetailParam upOrderDetail(OrderUpReqDTO orderUpReqDTO);

    OrderUpChangeToTableResDTO changeToTable(OrderUpChangeToTableReqDTO orderUpChangeToTableReqDTO);

    Order save(Order order);

    void delete(Order order);

    void deleteById(Long id);

    BigDecimal getOrderTotalAmount(Long orderId);

}
