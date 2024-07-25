package com.ecom2.order;

import com.ecom2.customer.dto.CustomerSignup;
import com.ecom2.order.dto.OrderDTO;
import com.ecom2.order.dto.ProductBillDTO;
import com.ecom2.order.dto.StatusOrderDTO;
import com.ecom2.order.entity.Order;
import com.ecom2.order.entity.OrderDetail;
import com.ecom2.order.service.OrderDetailService;
import com.ecom2.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    OrderDetailService orderDetailService;

    @GetMapping("/get-all-orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<Order> orders = orderService.getAllOrders();
        List<OrderDTO> orderDTOs = orderService.convertToOrderDTOs(orders);
        return ResponseEntity.ok(orderDTOs);
    }

    @PutMapping("/update-order-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestBody StatusOrderDTO request) {
        try {
            orderService.updateOrderStatus(orderId, request.getStatus());
            return ResponseEntity.ok().body("Order status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order status: " + e.getMessage());
        }
    }

    @GetMapping("/orders/{orderId}")
    public ResponseEntity<CustomerSignup> getOrder(@PathVariable Long orderId) {

        Order order = orderService.findById(orderId);
        CustomerSignup customer = new CustomerSignup();

        customer.setName(order.getCustomer().getName());
        customer.setPhone(order.getCustomer().getPhone());
        customer.setEmail(order.getCustomer().getUser().getEmail());
        customer.setCity(order.getAddress().getCity());
        customer.setDistrict(order.getAddress().getDistrict());
        customer.setCommune(order.getAddress().getCommune());
        customer.setAddress(order.getAddress().getAddress());


        return ResponseEntity.ok(customer);
    }

    @GetMapping("orders/{orderId}/details")
    public ResponseEntity<List<ProductBillDTO>> getOrderDetails(@PathVariable Long orderId) {
        List<OrderDetail> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductBillDTO> productBillDTOS = new ArrayList<>();
        for(OrderDetail x : orderDetails){
            ProductBillDTO product = new ProductBillDTO();
            product.setProductCode("" + x.getProduct().getProductId());
            product.setProductName(x.getProduct().getName());
            product.setQuantity(x.getQuantity());
            product.setTotalAmount(x.getPrice());
            productBillDTOS.add(product);
        }
        return ResponseEntity.ok(productBillDTOS);
    }
}
