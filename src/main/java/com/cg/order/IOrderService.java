package com.cg.order;


import com.cg.order.dto.OrderCreReqDTO;
import com.cg.order.dto.OrderUpChangeToTableReqDTO;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.order.dto.OrderUpReqDTO;
import com.cg.model.*;
import com.cg.orderDetail.dto.OrderDetailCreResDTO;
import com.cg.orderDetail.dto.OrderDetailUpResDTO;
import com.cg.service.IGeneralService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IOrderService  {
    Order findByTableId(Long tableId);

    List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid);


    OrderDetailCreResDTO creOrder(OrderCreReqDTO orderCreReqDTO, TableOrder tableOrder, User user);

    OrderDetailUpResDTO upOrderDetail(OrderUpReqDTO orderUpReqDTO, Order order, Product product, User user);

    OrderUpChangeToTableResDTO changeToTable(OrderUpChangeToTableReqDTO orderUpChangeToTableReqDTO, User user);

    Order save(Order order);

    void delete(Order order);

    void deleteById(Long id);

    BigDecimal getOrderTotalAmount(Long orderId);

}
