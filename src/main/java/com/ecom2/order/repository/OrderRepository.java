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

    @Query("SELECT COUNT(o) > 0 FROM Order o " +
            "JOIN o.orderItems oi " +
            "WHERE o.customer.user.userName = :userName " +
            "AND oi.product.productId = :productId " +
            "AND o.status = 'COMPLETED'")
    boolean hasPurchasedProduct(String userName, Long productId);
}
