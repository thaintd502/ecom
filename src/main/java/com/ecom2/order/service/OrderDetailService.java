package com.ecom2.order.service;

import com.ecom2.order.entity.OrderDetail;
import com.ecom2.order.repository.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface OrderDetailService {
    List<OrderDetail> getOrderDetailsByOrderId(Long orderId);
}
