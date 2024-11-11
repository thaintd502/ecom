package com.ecom2.cart.repository;

import com.ecom2.cart.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    @Query("SELECT c FROM Cart c WHERE c.user.userName = :userName AND c.cartId = :cartId")
    Cart findCartByUserNameAndCartId(String userName, Long cartId);
//
//    @Query("SELECT c FROM Cart c WHERE c.product.productId = :productId")
//    Cart findCartByProductId(Long productId);

    List<Cart> findAllByUser_UserId(int userId);
//    void deleteByProduct_ProductIdAndUser_UserId(Long productId, int userId);

    Cart findByUser_UserId(int userId);


}
