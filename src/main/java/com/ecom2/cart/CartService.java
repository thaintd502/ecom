package com.ecom2.cart;

import com.ecom2.cart.entity.Cart;

import java.util.List;

public interface CartService {

//    Cart addToCart(Long productId, int quantity, String userName);
//    List<Cart> getAllCart(String userName);
//    void deleteCartByProductId(Long productId, String userName);

    CartDTO addProductToCart(String userName, Long productId, int quantity);
    List<CartDTO> getAllCarts(String userName);
}
