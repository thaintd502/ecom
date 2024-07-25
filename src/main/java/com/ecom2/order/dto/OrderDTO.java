package com.ecom2.order.dto;

import lombok.Data;
import java.util.Date;

@Data
public class OrderDTO {
    private Long orderId;
    private String customerName;
    private String customerPhone;
    private Date orderDate;
    private double totalAmount;
    private double shippingFee;
    private String status;
}
