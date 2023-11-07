package com.cg.orderDetail;

import com.cg.exception.DataInputException;
import com.cg.model.Order;
import com.cg.model.OrderDetail;
import com.cg.orderDetail.dto.OrderDetailByTableResDTO;
import com.cg.order.OrderRepository;
import com.cg.order.IOrderService;
import com.cg.utils.ValidateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements IOrderDetailService{

    private final OrderDetailRepository orderDetailRepository;


    private final OrderRepository orderRepository;


    private final IOrderService orderService;
    private final ValidateUtils validateUtils;
    @Override
    public List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    @Override
    public Optional<OrderDetail> findById(Long id) {
        return orderDetailRepository.findById(id);
    }



    @Override
    public List<OrderDetailByTableResDTO> getOrderDetailByTableResDTO(String tableIdStr) {
        if (!validateUtils.isNumberValid(tableIdStr)) {
            throw new DataInputException("Mã số bàn không hợp lệ vui lòng xem lại");
        }

        Long tableId = Long.valueOf(tableIdStr);

        Order order = orderService.findByTableId(tableId);
        return orderDetailRepository.getOrderDetailByTableResDTO(order.getId());
    }

    @Override
    public Optional<OrderDetail> findByOrderDetailByIdProductAndIdOrder(Long idProduct, Long idOrder, String note) {
        return Optional.empty();
    }

    @Override
    public OrderDetail findByOrderId(Long orderId) {
        return null;
    }


    @Override
    public OrderDetail save(OrderDetail orderDetail) {
        return orderDetailRepository.save(orderDetail);
    }

    @Override
    public void delete(OrderDetail orderDetail) {
        Order order = orderDetail.getOrder();
        orderDetailRepository.delete(orderDetail);

        order.setOrderDetails(order.getOrderDetails()
                .stream().filter(e -> !Objects.equals(e.getId(), orderDetail.getId()))
                .collect(Collectors.toList()));

        if (order.getOrderDetails().size() == 0) {
            orderRepository.delete(order);
        }
        else {
            BigDecimal totalAmount = orderService.getOrderTotalAmount(order.getId());
            order.setTotalAmount(totalAmount);
            orderService.save(order);
        }
    }
    @Override
    public void deleteById(Long id) {

    }




}
