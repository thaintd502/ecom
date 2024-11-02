package com.ecom2.order.service;

import com.ecom2.order.entity.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> getOrderDetailsByOrderId(Long orderId);
}
