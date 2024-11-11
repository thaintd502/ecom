package com.ecom2.product.dto;

import com.ecom2.brand.Brand;
import com.ecom2.category.Category;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class ProductDTO {

    private Long productId;
    private String name;
    private String brand;
    private double price;
    private double promotePrice;
    private double importPrice;
    private int stockQuantity;
    private int discount;
    private String productCode;
    private int importQuantity;
    private int availableStock;
    private String description;
    private String imageUrl;
    private int sold;
    private Set<Category> categories = new HashSet<>();
}
