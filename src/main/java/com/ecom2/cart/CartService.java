package com.ecom2.cart;

import com.ecom2.cart.dto.CartDTO;
import com.ecom2.cart.dto.CartItemDTO;
import com.ecom2.cart.entity.Cart;
import com.ecom2.cart.entity.CartItem;

import java.util.List;

public interface CartService {

    CartDTO addProductToCart(String userName, Long productId, int quantity);
    List<CartDTO> getAllCarts(String userName);
    void deleteCartItemByProductIdAndCartId(Long cartId, Long productId);
    CartItemDTO updateProductQuantityInCart(CartItemDTO cartItemDTO);
    List<CartItemDTO> viewCart(String userName);
    void deleteCartItemById(Long cartItemId);
    void saveCart(Cart cart);
}
