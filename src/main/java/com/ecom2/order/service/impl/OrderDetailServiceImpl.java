package com.ecom2.order.service.impl;

import com.ecom2.order.entity.OrderDetail;
import com.ecom2.order.repository.OrderDetailRepository;
import com.ecom2.order.service.OrderDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderDetailServiceImpl implements OrderDetailService {

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return orderDetailRepository.findByOrderOrderId(orderId);
    }
}
