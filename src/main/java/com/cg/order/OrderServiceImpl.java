package com.cg.order;

import com.cg.exception.ResourceNotFoundException;
import com.cg.order.dto.OrderCreReqDTO;
import com.cg.order.dto.OrderUpChangeToTableReqDTO;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.order.dto.OrderUpReqDTO;
import com.cg.exception.DataInputException;
import com.cg.model.*;
import com.cg.orderDetail.dto.OrderDetailCreResDTO;
import com.cg.orderDetail.dto.OrderDetailProductUpResDTO;
import com.cg.orderDetail.dto.OrderDetailUpResDTO;
import com.cg.model.enums.ETableStatus;
import com.cg.orderDetail.OrderDetailRepository;
import com.cg.product.ProductRepository;
import com.cg.staff.StaffRepository;
import com.cg.tableOrder.TableOrderMapper;
import com.cg.tableOrder.TableOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements IOrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final StaffRepository staffRepository;
    private final TableOrderRepository tableOrderRepository;
    private final ProductRepository productRepository;
    private final TableOrderMapper tableOrderMapper;

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public Order findByTableId(Long tableId) {
        return orderRepository.findByTableId(tableId).orElseThrow(() -> new ResourceNotFoundException("Not found"));
    }

    @Override
    public List<Order> findByTableOrderAndPaid(TableOrder tableOrder, Boolean paid) {
        return orderRepository.findByTableOrderAndPaid(tableOrder, paid);
    }

    @Override
    public OrderDetailCreResDTO creOrder(OrderCreReqDTO orderCreReqDTO, TableOrder tableOrder, User user) {
        Order order = new Order();
        Staff staff = staffRepository.findStaffByUserAndDeletedIsFalse(user);
        order.setStaff(staff);
        order.setTableOrder(tableOrder);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPaid(false);
        orderRepository.save(order);

        tableOrder.setStatus(ETableStatus.BUSY);
        tableOrderRepository.save(tableOrder);

        Product product = productRepository.findById(orderCreReqDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));

        OrderDetail orderDetail = new OrderDetail();

        Long quantity = orderCreReqDTO.getQuantity();
        BigDecimal price = product.getPrice();
        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(price);
        orderDetail.setAmount(amount);
        orderDetail.setNote(orderCreReqDTO.getNote());
        orderDetail.setOrder(order);

        orderDetailRepository.save(orderDetail);

        order.setTotalAmount(amount);
        orderRepository.save(order);

        OrderDetailCreResDTO orderDetailCreResDTO = new OrderDetailCreResDTO();
        orderDetailCreResDTO.setOrderDetailId(orderDetail.getId());
        orderDetailCreResDTO.setTable(tableOrderMapper.toDTO(tableOrder));
        orderDetailCreResDTO.setProductId(product.getId());
        orderDetailCreResDTO.setTitle(product.getTitle());
        orderDetailCreResDTO.setPrice(price);
        orderDetailCreResDTO.setQuantity(quantity);
        orderDetailCreResDTO.setAmount(amount);
        orderDetailCreResDTO.setNote(orderDetail.getNote());
        orderDetailCreResDTO.setTotalAmount(amount);
        orderDetailCreResDTO.setAvatar(product.getProductAvatar().toProductAvatarDTO());

        return orderDetailCreResDTO;
    }

    @Override
    public OrderDetailUpResDTO upOrderDetail(OrderUpReqDTO orderUpReqDTO, Order order, Product product, User user) {
        List<OrderDetail> orderDetails = orderDetailRepository.findAllByOrder(order);
        OrderDetail orderDetail = new OrderDetail();

        if (orderDetails.size() == 0) {
            throw new DataInputException("Hoá đơn bàn này chưa có mặt hàng nào, vui lòng liên hệ ADMIN để kiểm tra lại dữ liệu");
        }

        Optional<OrderDetail> orderDetailOptional = orderDetailRepository.findByProductIdAndOrderIdAndNote(orderUpReqDTO.getProductId(), order.getId(), orderUpReqDTO.getNote());

        if (orderDetailOptional.isEmpty()) {
            Long quantity = orderUpReqDTO.getQuantity();
            BigDecimal price = product.getPrice();
            BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

            orderDetail.setProduct(product);
            orderDetail.setOrder(order);
            orderDetail.setPrice(product.getPrice());
            orderDetail.setQuantity(quantity);
            orderDetail.setAmount(amount);
            orderDetail.setNote(orderUpReqDTO.getNote());
            orderDetailRepository.save(orderDetail);

            BigDecimal newTotalAmount = getOrderTotalAmount(order.getId());
            order.setTotalAmount(newTotalAmount);
            orderRepository.save(order);
        } else {
            orderDetail = orderDetailOptional.get();
            long newQuantity = orderDetail.getQuantity() + orderUpReqDTO.getQuantity();
            BigDecimal price = orderDetail.getPrice();
            BigDecimal newAmount = price.multiply(BigDecimal.valueOf(newQuantity));
            orderDetail.setQuantity(newQuantity);
            orderDetail.setAmount(newAmount);
            orderDetailRepository.save(orderDetail);

            BigDecimal newTotalAmount = getOrderTotalAmount(order.getId());
            order.setTotalAmount(newTotalAmount);
            orderRepository.save(order);
        }

        List<OrderDetailProductUpResDTO> newOrderDetails = orderDetailRepository.findAllOrderDetailProductUpResDTO(order.getId());

        OrderDetailUpResDTO orderDetailUpResDTO = new OrderDetailUpResDTO();
        orderDetailUpResDTO.setTable(tableOrderMapper.toDTO(order.getTableOrder()));
        orderDetailUpResDTO.setProducts(newOrderDetails);
        orderDetailUpResDTO.setTotalAmount(order.getTotalAmount());

        return orderDetailUpResDTO;

    }

    @Override
    public OrderUpChangeToTableResDTO changeToTable(OrderUpChangeToTableReqDTO orderUpChangeToTableReqDTO, User user) {
        Order orderBusy = orderRepository.findByTableId(orderUpChangeToTableReqDTO.getTableIdBusy()).get();

        TableOrder emptyTable = tableOrderRepository.findById(orderUpChangeToTableReqDTO.getTableIdEmpty()).get();
        emptyTable.setStatus(ETableStatus.BUSY);
        tableOrderRepository.save(emptyTable);

        orderBusy.setTableOrder(emptyTable);
        orderRepository.save(orderBusy);

        TableOrder busyTable = tableOrderRepository.findById(orderUpChangeToTableReqDTO.getTableIdBusy()).get();
        busyTable.setStatus(ETableStatus.EMPTY);
        tableOrderRepository.save(busyTable);

        List<OrderDetailProductUpResDTO> newOrderDetails = orderDetailRepository.findAllOrderDetailProductUpResDTO(orderBusy.getId());

        OrderUpChangeToTableResDTO orderUpChangeToTableResDTO = new OrderUpChangeToTableResDTO();
        orderUpChangeToTableResDTO.setTableOrder(
                tableOrderMapper.toDTO(orderBusy.getTableOrder()));
        orderUpChangeToTableResDTO.setTotalAmount(orderBusy.getTotalAmount());
        orderUpChangeToTableResDTO.setProducts(newOrderDetails);


        return orderUpChangeToTableResDTO;
    }

    @Override
    public Order save(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void delete(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public void deleteById(Long id) {

    }

    public BigDecimal getOrderTotalAmount(Long orderId) {
        return orderRepository.getOrderTotalAmount(orderId);
    }
}
