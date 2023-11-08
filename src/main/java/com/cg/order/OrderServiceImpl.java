package com.cg.order;

import com.cg.exception.ResourceNotFoundException;
import com.cg.model.user.User;
import com.cg.order.dto.CreationOrderParam;
import com.cg.order.dto.OrderUpChangeToTableReqDTO;
import com.cg.order.dto.OrderUpChangeToTableResDTO;
import com.cg.order.dto.OrderUpReqDTO;
import com.cg.exception.DataInputException;
import com.cg.model.*;
import com.cg.orderDetail.OrderDetailMapper;
import com.cg.orderDetail.dto.CreationOrderDetailParam;
import com.cg.orderDetail.dto.OrderDetailProductUpResDTO;
import com.cg.orderDetail.dto.UpdateOrderDetailParam;
import com.cg.model.enums.ETableStatus;
import com.cg.orderDetail.OrderDetailRepository;
import com.cg.product.IProductService;
import com.cg.product.ProductRepository;
import com.cg.staff.StaffRepository;
import com.cg.tableOrder.ITableOrderService;
import com.cg.tableOrder.TableOrderMapper;
import com.cg.tableOrder.TableOrderRepository;
import com.cg.user.UserServiceImpl;
import com.cg.utils.AppUtils;
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
    private final OrderDetailMapper orderDetailMapper;
    private final AppUtils appUtils;
    private final UserServiceImpl userService;
    private final IProductService productService;
    private final ITableOrderService tableOrderService;
//    private final IOrderService orderService;

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
    public CreationOrderDetailParam creOrder(CreationOrderParam creationOrderParam, TableOrder tableOrder, User user) {
        Order order = new Order();
        Staff staff = staffRepository.findStaffByUserAndDeletedIsFalse(user);
        order.setStaff(staff);
        order.setTableOrder(tableOrder);
        order.setTotalAmount(BigDecimal.ZERO);
        order.setPaid(false);
        orderRepository.save(order);

        tableOrder.setStatus(ETableStatus.BUSY);
        tableOrderRepository.save(tableOrder);

        Product product = productRepository.findById(creationOrderParam.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Not found!"));

        OrderDetail orderDetail = new OrderDetail();

        Long quantity = creationOrderParam.getQuantity();
        BigDecimal price = product.getPrice();
        BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));

        orderDetail.setProduct(product);
        orderDetail.setQuantity(quantity);
        orderDetail.setPrice(price);
        orderDetail.setAmount(amount);
        orderDetail.setNote(creationOrderParam.getNote());
        orderDetail.setOrder(order);

        orderDetailRepository.save(orderDetail);

        order.setTotalAmount(amount);
        orderRepository.save(order);
        return orderDetailMapper.toDTO(orderDetail,tableOrder,product,order);


    }

    @Override
    public UpdateOrderDetailParam upOrderDetail(OrderUpReqDTO orderUpReqDTO) {
        String username = appUtils.getPrincipalUsername();
        User user = userService.findByUsername(username);


        TableOrder tableOrder = tableOrderService.findById(orderUpReqDTO.getTableId());

        Product product = productService.findById(orderUpReqDTO.getProductId());

        List<Order> orders = this.findByTableOrderAndPaid(tableOrder, false);

        if (orders.size() == 0) {
            throw new DataInputException("Bàn này không có hoá đơn, vui lòng kiểm tra lại thông tin");
        }

        if (orders.size() > 1) {
            throw new DataInputException("Lỗi hệ thống, vui lòng liên hệ ADMIN để kiểm tra lại dữ liệu");
        }

        Order order = orders.get(0);
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
        return orderDetailMapper.toDTOList(newOrderDetails,order,tableOrderMapper);

    }

    @Override
    public OrderUpChangeToTableResDTO changeToTable(OrderUpChangeToTableReqDTO orderUpChangeToTableReqDTO) {
        String username = appUtils.getPrincipalUsername();
//        User user = userService.findByUsername(username);

        TableOrder tableOrderBusy = tableOrderService.findById(orderUpChangeToTableReqDTO
                .getTableIdBusy());
        TableOrder tableOrderEmpty = tableOrderService.findById(orderUpChangeToTableReqDTO
                .getTableIdEmpty());

        if (tableOrderEmpty.getId().equals(tableOrderBusy.getId())) {
            throw new DataInputException("Bàn chuyển và bàn chuyển là một xin vui lòng kiểm tra lại");
        }

        List<Order> orderEmptys = this.findByTableOrderAndPaid(tableOrderEmpty, false);
        List<Order> orderBusys = this.findByTableOrderAndPaid(tableOrderBusy, false);

        if (orderBusys.size() == 0) {
            throw new DataInputException("Bàn chuyển này không có hoá đơn, vui lòng kiểm tra lại thông tin");
        }

        if (orderBusys.size() > 1) {
            throw new DataInputException("Lỗi hệ thống, vui lòng liên hệ ADMIN để kiểm tra lại dữ liệu");
        }

        if (orderEmptys.size() != 0) {
            throw new DataInputException("Bàn nhận này đang có hoá đơn, vui lòng kiểm tra lại thông tin");
        }
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

//        OrderUpChangeToTableResDTO orderUpChangeToTableResDTO = new OrderUpChangeToTableResDTO();
//        orderUpChangeToTableResDTO.setTableOrder(tableOrderMapper.toDTO(orderBusy.getTableOrder()));
//        orderUpChangeToTableResDTO.setTotalAmount(orderBusy.getTotalAmount());
//        orderUpChangeToTableResDTO.setProducts(newOrderDetails);
//
//
//        return orderUpChangeToTableResDTO;
        return orderDetailMapper.toDTOUpChang(tableOrderMapper,newOrderDetails,orderBusy);
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
