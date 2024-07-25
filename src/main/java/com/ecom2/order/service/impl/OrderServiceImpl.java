package com.ecom2.order.service.impl;

import com.ecom2.order.dto.OrderDTO;
import com.ecom2.order.entity.Order;
import com.ecom2.order.repository.OrderRepository;
import com.ecom2.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;


    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public List<OrderDTO> convertToOrderDTOs(List<Order> orders) {
        return orders.stream().map(order -> {
            OrderDTO dto = new OrderDTO();
            dto.setOrderId(order.getOrderId());
            dto.setCustomerName(order.getCustomer().getName());
            dto.setCustomerPhone(order.getCustomer().getPhone());
            dto.setOrderDate(order.getOrderDate());
            dto.setTotalAmount(order.getTotalAmount());
            dto.setShippingFee(order.getShippingFee());
            dto.setStatus(order.getStatus());
            return dto;
        }).collect(Collectors.toList());
    }

    public void updateOrderStatus(Long orderId, String newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(newStatus);
        orderRepository.save(order);
    }

    public Order findById(Long orderId){
        Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isPresent()) {
            Order order = orderOptional.get();
            return order;
        } else {
            throw new RuntimeException("Order not found");
        }
    }
}
