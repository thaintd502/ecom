package com.ecom2.payment;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final ZaloPayService zaloPayService;

    public PaymentController(ZaloPayService zaloPayService) {
        this.zaloPayService = zaloPayService;
    }

    @PostMapping("/public/create-order")
    public String createOrder(@RequestParam long amount) {
        try {
            return zaloPayService.createOrder(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to create order";
        }
    }

}
