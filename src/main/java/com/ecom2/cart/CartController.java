package com.ecom2.cart;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.cart.dto.CartDTO;
import com.ecom2.cart.dto.CartItemDTO;
import com.ecom2.cart.entity.CartItem;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/public")
@RequiredArgsConstructor
public class CartController {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private CartService cartService;

    @PostMapping("/add-product-to-cart/{productId}")
    public ResponseEntity<?> addToCart(
            @PathVariable Long productId,
            @RequestParam int quantity,
            @RequestHeader("Authorization") String token) {

        String jwtToken = token.replace("Bearer ", "");
        String userName = jwtTokenProvider.getUserNameFromJwt(jwtToken);

        if (userName == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        try {
            CartDTO cartDTO = cartService.addProductToCart(userName, productId, quantity);
            return ResponseEntity.ok(cartDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product to cart: " + e.getMessage());
        }
    }

    @GetMapping("/view-cart")
    public ResponseEntity<?> viewCart(@RequestHeader("Authorization") String token){

        String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));

        if(userName == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }

        List<CartDTO> cartDTOS = cartService.getAllCarts(userName);
        if(cartDTOS.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty");
        }

        return ResponseEntity.ok(cartDTOS);
    }

    @PutMapping("/carts/update-product-quantity")
    public ResponseEntity<?> updateProductQuantityInCart(@RequestBody CartItemDTO cartItemDTO){
        try{
            CartItemDTO cartItem = cartService.updateProductQuantityInCart(cartItemDTO);
            return ResponseEntity.ok(cartItem);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/carts/delete-cart-item/{cartId}/{productId}")
    public ResponseEntity<?> deleteCartItemByCartIdAndProductId(@PathVariable Long cartId,
                                                                @PathVariable Long productId){
        try{
            cartService.deleteCartItemByProductIdAndCartId(cartId, productId);
            return ResponseEntity.ok("Cart item deleted successfully.");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting cart item: " + e.getMessage());
        }
    }






}
