package com.ecom2.order.service.impl;

import com.ecom2.cart.CartService;
import com.ecom2.cart.entity.Cart;
import com.ecom2.cart.entity.CartItem;
import com.ecom2.cart.repository.CartRepository;
import com.ecom2.customer.entity.Customer;
import com.ecom2.customer.service.CustomerService;
import com.ecom2.exception.APIException;
import com.ecom2.exception.ResourceNotFoundException;
import com.ecom2.order.dto.OrderDTO;
import com.ecom2.order.dto.OrderItemDTO;
import com.ecom2.order.entity.EStatus;
import com.ecom2.order.entity.Order;
import com.ecom2.order.entity.OrderItem;
import com.ecom2.order.repository.OrderItemRepository;
import com.ecom2.order.repository.OrderRepository;
import com.ecom2.order.service.OrderService;
import com.ecom2.payment.Payment;
import com.ecom2.payment.PaymentRepository;
import com.ecom2.product.entity.Product;
import com.ecom2.product.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartService cartService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public OrderServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

//    public List<OrderDTO> convertToOrderDTOs(List<Order> orders) {
//        return orders.stream().map(order -> {
//            OrderDTO dto = new OrderDTO();
//            dto.setOrderId(order.getOrderId());
//            dto.setCustomerName(order.getCustomer().getName());
//            dto.setCustomerPhone(order.getCustomer().getPhone());
//            dto.setOrderDate(order.getOrderDate());
//            dto.setTotalAmount(order.getTotalAmount());
////            dto.setShippingFee(order.getShippingFee());
//            dto.setStatus(order.getStatus());
//            return dto;
//        }).collect(Collectors.toList());
//    }

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

    @Override
    public List<OrderDTO> getOrdersByUser(String userName) {

        Customer customer = customerService.findByUserName(userName);
        if(customer == null){
            throw new ResourceNotFoundException("Customer", "userName", userName);
        }
        List<Order> orders = orderRepository.findByCustomer_CustomerId(customer.getCustomerId());

        return orders.stream()
                .map(order -> modelMapper.map(order, OrderDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderByUser(String userName, Long orderId) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        Customer customer = customerService.findByUserName(userName);
        Order order = orderRepository.findByOrderIdAndCustomerId(orderId, customer.getCustomerId());
        return modelMapper.map(order, OrderDTO.class);
    }

    @Override
    public OrderDTO placeOrder(String userName, Long cartId, String paymentMethod) {
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        Cart cart = cartRepository.findCartByUserNameAndCartId(userName, cartId);
        if(cart == null){
            throw new ResourceNotFoundException("Cart", "cartId", cartId);
        }

        Order order = new Order();

        order.setStatus(EStatus.PENDING.toString());
        order.setOrderDate(new Date());
        order.setTotalAmount(cart.getTotalPrice());

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setPaymentMethod(paymentMethod);

        paymentRepository.save(payment);

        order.setPayment(payment);

        Order saveOrder = orderRepository.save(order);

        List<CartItem> cartItems = cart.getCartItems();

        if(cartItems.size() == 0){
            throw new APIException("Cart is empty");
        }

        List<OrderItem> orderItems = new ArrayList<>();

        for(CartItem x : cartItems){
            OrderItem orderItem = new OrderItem();

            orderItem.setProduct(x.getProduct());
            orderItem.setOrder(saveOrder);
            orderItem.setQuantity(x.getQuantity());
            orderItem.setDiscount(x.getDiscount());
            orderItem.setProductPrice(x.getProductPrice());

            orderItems.add(orderItem);
        }

        orderItemRepository.saveAll(orderItems);
//        saveOrder.setOrderItems(orderItems);

        cart.getCartItems().forEach(item -> {
            int quantity = item.getQuantity();

            Product product = item.getProduct();

            cartService.deleteCartItemByProductIdAndCartId(cartId, item.getProduct().getProductId());

            product.setStockQuantity(product.getStockQuantity() - quantity);
            product.setSold(product.getSold() + quantity);

            productRepository.save(product);
        });

        OrderDTO orderDTO = modelMapper.map(saveOrder, OrderDTO.class);

        orderItems.forEach(item -> orderDTO.getOrderItems().add(modelMapper.map(item, OrderItemDTO.class)));

        return orderDTO;
    }


//    private OrderDTO convertToOrderDTO(Order order) {
//        OrderDTO orderDTO = new OrderDTO();
//        orderDTO.setOrderId(order.getOrderId());
//        orderDTO.setCustomerName(order.getCustomer().getName());
//        orderDTO.setCustomerPhone(order.getCustomer().getPhone());
//        orderDTO.setOrderDate(order.getOrderDate());
//        orderDTO.setTotalAmount(order.getTotalAmount());
////        orderDTO.setShippingFee(order.getShippingFee());
//        orderDTO.setStatus(order.getStatus());
//        return orderDTO;
//    }
}
