package com.ecom2.order.service.impl;

import com.ecom2.order.entity.OrderItem;
import com.ecom2.order.repository.OrderItemRepository;
import com.ecom2.order.service.OrderItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    @Autowired
    private OrderItemRepository orderDetailRepository;

    public List<OrderItem> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }
}
