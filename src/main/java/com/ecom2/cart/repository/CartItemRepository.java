package com.ecom2.cart.repository;

import com.ecom2.cart.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);

    void deleteByCart_CartIdAndProduct_ProductId(Long cartId, Long productId);

}
