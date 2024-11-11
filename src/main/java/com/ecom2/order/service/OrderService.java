package com.ecom2.order.service;

import com.ecom2.order.dto.OrderDTO;
import com.ecom2.order.entity.Order;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();
//    List<OrderDTO> convertToOrderDTOs(List<Order> orders);
    void updateOrderStatus(Long orderId, String newStatus);
    Order findById(Long orderId);
    List<OrderDTO> getOrdersByUser(String userName);
    OrderDTO getOrderByUser(String userName, Long orderId);
    OrderDTO placeOrder(String userName, Long cartId, String paymentMethod);
}
