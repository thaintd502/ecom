package com.ecom2.payment;

import com.ecom2.payment.dto.VNPayResponse;
import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    VNPayResponse createVnPayPayment(HttpServletRequest request);
}
