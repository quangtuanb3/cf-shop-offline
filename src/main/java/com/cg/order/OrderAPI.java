package com.cg.order;

import com.cg.exception.DataInputException;
import com.cg.model.*;
import com.cg.model.user.User;
import com.cg.order.dto.CreationOrderParam;
import com.cg.order.dto.OrderUpChangeToTableReqDTO;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.order.dto.OrderUpReqDTO;
import com.cg.orderDetail.dto.OrderDetailByTableResDTO;
import com.cg.orderDetail.dto.UpdateOrderDetailParam;
import com.cg.model.enums.ETableStatus;
import com.cg.orderDetail.IOrderDetailService;
import com.cg.tableOrder.ITableOrderService;
import com.cg.user.IUserService;
import com.cg.utils.AppUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderAPI {
    private final IOrderService orderService;
    private final IOrderDetailService orderDetailService;
    private final ITableOrderService tableOrderService;
    private final IUserService userService;
    private final AppUtils appUtils;

    @GetMapping("/table/{tableId}")
    public ResponseEntity<?> getOrderByTableId(@PathVariable("tableId") String tableIdStr) {
        List<OrderDetailByTableResDTO> orderDetails = orderDetailService.getOrderDetailByTableResDTO(tableIdStr);
        return new ResponseEntity<>(orderDetails, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<?> creOrder(@RequestBody CreationOrderParam creationOrderParam) {
        String username = appUtils.getPrincipalUsername();
        User user = userService.findByUsername(username);
        TableOrder tableOrder = tableOrderService.findById(creationOrderParam.getTableId());
        List<Order> orders = orderService.findByTableOrderAndPaid(tableOrder, false);
        if (orders.size() > 0) {
            throw new DataInputException("Bàn này đang có hoá đơn, vui lòng kiểm tra lại thông tin");
        }

        if (tableOrder.getStatus() == ETableStatus.EMPTY) {


            return new ResponseEntity<>(orderService.creOrder(creationOrderParam, tableOrder, user), HttpStatus.CREATED);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/update")
    public ResponseEntity<?> upOrder(@RequestBody OrderUpReqDTO orderUpReqDTO) {
            UpdateOrderDetailParam updateOrderDetailParam = orderService
                    .upOrderDetail(orderUpReqDTO);
            return new ResponseEntity<>(updateOrderDetailParam, HttpStatus.OK);


    }

    @DeleteMapping("/delete/{orderDetailId}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long orderDetailId) {
//        OrderDetail orderDetail = orderDetailService.findById(orderDetailId)
//                .orElseThrow(() -> new DataInputException("Vui lòng kiểm tra lại thông tin"));
//
//        Long orderId = orderDetail.getOrder().getId();
//        Long tableId = orderDetail.getOrder().getTableOrder().getId();
//
//        orderDetailService.delete(orderDetail);
//        List<OrderDetailByTableResDTO> orderDetails = orderDetailService
//                .getOrderDetailByTableResDTO(orderId);
//        if (orderDetails.isEmpty()) {
//            TableOrder tableOrder = tableOrderService.findById(tableId);
//            tableOrder.setStatus(ETableStatus.EMPTY);
//            tableOrderService.save(tableOrder);
//            return new ResponseEntity<>(tableOrder, HttpStatus.OK);
//        }
//        return new ResponseEntity<>(orderDetail.getOrder().getTableOrder(), HttpStatus.OK);
        return null;
    }

    @PatchMapping("/update/change-to-table")
    public ResponseEntity<?> changeToTable(@RequestBody OrderUpChangeToTableReqDTO orderUpChangeToTableReqDTO) {



        OrderUpChangeToTableResDTO orderUpChangeToTableResDTO = orderService
                .changeToTable(orderUpChangeToTableReqDTO);

        return new ResponseEntity<>(orderUpChangeToTableResDTO, HttpStatus.OK);

    }
}
