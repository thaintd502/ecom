package com.ecom2.order.repository;

import com.ecom2.order.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer_CustomerId(Long customerId);
    @Query("SELECT o FROM Order o WHERE o.orderId = :orderId AND o.customer.id = :customerId")
    Order findByOrderIdAndCustomerId(Long orderId, Long customerId);
}
