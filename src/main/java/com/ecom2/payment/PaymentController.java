package com.ecom2.payment;

import com.ecom2.payment.dto.ResponseObject;
import com.ecom2.payment.dto.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class PaymentController {

    private final ZaloPayService zaloPayService;

    @Autowired
    private PaymentService paymentService;

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

    @GetMapping("/public/vn-pay")
    public ResponseObject<VNPayResponse> pay(HttpServletRequest request) {
        return new ResponseObject<>(HttpStatus.OK, "Success", paymentService.createVnPayPayment(request));
    }
    @GetMapping("/public/vn-pay-callback")
    public ResponseObject<VNPayResponse> payCallbackHandler(HttpServletRequest request) {
        String status = request.getParameter("vnp_ResponseCode");
        if (status.equals("00")) {
            return new ResponseObject<>(HttpStatus.OK, "Success", new VNPayResponse("00", "Success", ""));
        } else {
            return new ResponseObject<>(HttpStatus.BAD_REQUEST, "Failed", null);
        }
    }

}
