package com.ecom2.order.dto;

import lombok.Data;

@Data
public class ProductBillDTO {
    private String productCode;
    private String productName;
    private int quantity;
    private double totalAmount;
}
