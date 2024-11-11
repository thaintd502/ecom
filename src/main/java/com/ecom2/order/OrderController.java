package com.ecom2.order;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.customer.dto.CustomerDTO;
import com.ecom2.order.dto.OrderDTO;
import com.ecom2.order.dto.ProductBillDTO;
import com.ecom2.order.entity.Order;
import com.ecom2.order.entity.OrderItem;
import com.ecom2.order.service.OrderItemService;
import com.ecom2.order.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

//    @GetMapping("/admin/get-all-orders")
//    public ResponseEntity<List<OrderDTO>> getAllOrders() {
//        List<Order> orders = orderService.getAllOrders();
//        List<OrderDTO> orderDTOs = orderService.convertToOrderDTOs(orders);
//        return ResponseEntity.ok(orderDTOs);
//    }

    @PostMapping("/public/order-product/{cartId}/{paymentMethod}")
    public ResponseEntity<?> orderProducts(@PathVariable Long cartId,
                                           @PathVariable String paymentMethod,
                                           @RequestHeader("Authorization") String token){
//        try{
            String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
            OrderDTO orderDTO = orderService.placeOrder(userName, cartId, paymentMethod);
            return ResponseEntity.status(200).body(orderDTO);
//        }catch (Exception e){
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
    }

    @PutMapping("/admin/update-order-status/{orderId}")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long orderId, @RequestParam String status) {
        try {
            orderService.updateOrderStatus(orderId, status);
            return ResponseEntity.ok().body("Order status updated successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to update order status: " + e.getMessage());
        }
    }

    @GetMapping("public/orders/{orderId}")
    public ResponseEntity<?> getOrder(@PathVariable Long orderId,
                                      @RequestHeader("Authorization") String token) {

      try{
          String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
          OrderDTO orderDTO = orderService.getOrderByUser(userName, orderId);
          return ResponseEntity.ok(orderDTO);
      }catch (Exception e){
          return ResponseEntity.status(500).body("Error: " + e.getMessage());
      }
    }

    @GetMapping("public/orders/{orderId}/details")
    public ResponseEntity<List<ProductBillDTO>> getOrderDetails(@PathVariable Long orderId) {
        List<OrderItem> orderDetails = orderDetailService.getOrderDetailsByOrderId(orderId);
        if (orderDetails.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        List<ProductBillDTO> productBillDTOS = new ArrayList<>();
        for(OrderItem x : orderDetails){
            ProductBillDTO product = new ProductBillDTO();
            product.setProductCode("" + x.getProduct().getProductId());
            product.setProductName(x.getProduct().getName());
            product.setQuantity(x.getQuantity());
            product.setTotalAmount(x.getProductPrice());
            productBillDTOS.add(product);
        }
        return ResponseEntity.ok(productBillDTOS);
    }

    @GetMapping("public/orders/user")
    public ResponseEntity<?> getUserOrders(@RequestHeader("Authorization") String token){
        try{
            String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
            if(userName == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
            }

            List<OrderDTO> orders = orderService.getOrdersByUser(userName);
            if(orders.isEmpty()){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Order is enmpty");
            }
            return ResponseEntity.ok(orders);
        }catch (Exception e){
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }



    
}
