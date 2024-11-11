package com.ecom2.cart.dto;

import lombok.Data;

@Data
public class CartItemDTO {
    private Long cartId;
    private Long productId;
    private int quantity;
}
