package com.ecom2.product.dto;

import lombok.Data;

@Data
public class ProductDTO {

    private Long productId;
    private String productCode;
    private String productName;
    private String category;
    private String brand;
    private double importPrice;
    private double price;
    private double promotePrice;
    private int importQuantity;
    private String imageUrl;
    private String description;
}
