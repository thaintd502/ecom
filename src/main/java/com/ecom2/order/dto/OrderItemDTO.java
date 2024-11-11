package com.ecom2.order.dto;

import com.ecom2.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class OrderItemDTO {

    private Long orderItemId;
    private ProductDTO product;
    private int quantity;
    private double productPrice;
    private double discount;
}
