package com.ecom2.cart;

import com.ecom2.cart.entity.Cart;
import com.ecom2.cart.entity.CartItem;
import com.ecom2.cart.repository.CartItemRepository;
import com.ecom2.cart.repository.CartRepository;
import com.ecom2.product.Product;
import com.ecom2.product.ProductService;
import com.ecom2.product.dto.ProductDTO;
import com.ecom2.user.entity.User;
import com.ecom2.user.service.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService{

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartItemRepository cartItemRepository;

    private final ModelMapper modelMapper;

    @Autowired
    public CartServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
    @Override
    public CartDTO addProductToCart(String userName, Long productId, int quantity) {
        User user = userService.findByUserName(userName);

        Product product = productService.findById(productId).orElseThrow();

        Cart cart =  cartRepository.findByUser_UserId(user.getUserId());
        if(cart == null){
            cart = new Cart();
            cart.setUser(user);
            cart.setTotalPrice(0);
            cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cart.getCartId(), productId);

        if(cartItem == null){
            CartItem newCartItem = new CartItem();
            newCartItem.setProduct(product);
            newCartItem.setCart(cart);
            newCartItem.setQuantity(quantity);
            newCartItem.setDiscount(product.getDiscount());
            newCartItem.setProductPrice(product.getPrice());

            cartItemRepository.save(newCartItem);

        }else{
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItemRepository.save(cartItem);
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productService.saveProduct(product);

        cart.setTotalPrice(cart.getTotalPrice() + product.getPrice() * quantity);
        cartRepository.save(cart);

        CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

        List<ProductDTO> productDTOS = cart.getCartItems().stream()
                .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                .collect(Collectors.toList());

        cartDTO.setProducts(productDTOS);

        return cartDTO;

    }

    @Override
    public List<CartDTO> getAllCarts(String userName) {
        User user = userService.findByUserName(userName);

        List<Cart> carts = cartRepository.findAllByUser_UserId(user.getUserId());

        List<CartDTO> cartDTOS = carts.stream()
                .map(cart -> {
                    CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);

                    List<ProductDTO> productDTOS = cart.getCartItems().stream()
                            .map(p -> modelMapper.map(p, ProductDTO.class))
                            .collect(Collectors.toList());

                    cartDTO.setProducts(productDTOS);
                    return cartDTO;
                })
                .collect(Collectors.toList());

        return cartDTOS;
    }

//    @Override
//    public Cart addToCart(Long productId, int quantity, String userName) {
//        User user = userService.findByUserName(userName);
//
//        if(cartRepository.findCartByUserId(user.getUserId())) {
//            Cart cart = cartRepository.findCartByProductId(productId);
//            if(cart != null) {
//                cart.setQuantity(cart.getQuantity() + quantity);
//                cartRepository.save(cart);
//                return cart;
//            }
//        }
//
//        Cart newCart = new Cart();
//        newCart.setProduct(productService.findById(productId).orElseThrow(() ->
//                new RuntimeException("Product not found with ID: " + productId)));
//        newCart.setUser(user);
//        newCart.setQuantity(quantity);
//        cartRepository.save(newCart);
//        return newCart;
//    }
//
//    @Override
//    public List<Cart> getAllCart(String userName) {
//        User user = userService.findByUserName(userName);
//        return cartRepository.findAllByUser_UserId(user.getUserId());
//    }
//
//    @Transactional
//    public void deleteCartByProductId(Long productId, String userName) {
//        User user = userService.findByUserName(userName);
//        cartRepository.deleteByProduct_ProductIdAndUser_UserId(productId, user.getUserId());
//    }


}
