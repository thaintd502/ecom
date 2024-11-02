package com.ecom2.cart;

import java.util.ArrayList;
import java.util.List;

import com.ecom2.cart.entity.Cart;
import com.ecom2.product.dto.ProductDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

    private Long cartId;
    private double totalPrice = 0.0;
    private List<ProductDTO> products = new ArrayList<>();

}
