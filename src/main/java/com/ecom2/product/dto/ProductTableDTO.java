package com.ecom2.product.dto;

import lombok.Data;

import java.util.Set;

@Data
public class ProductTableDTO {
    private Long productId;
    private String name;
    private String category;
    private double price;
}
