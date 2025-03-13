package com.ecom2.cart.dto;

import com.ecom2.product.dto.ProductDTO;
import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartItemId;
    private ProductDTO product;
    private int quantity;
}
