package com.ecom2.cart;

import com.ecom2.cart.dto.CartDTO;
import com.ecom2.cart.dto.CartItemDTO;
import com.ecom2.cart.entity.Cart;
import com.ecom2.cart.entity.CartItem;
import com.ecom2.cart.repository.CartItemRepository;
import com.ecom2.cart.repository.CartRepository;
import com.ecom2.exception.APIException;
import com.ecom2.exception.ResourceNotFoundException;
import com.ecom2.product.entity.Product;
import com.ecom2.product.service.ProductService;
import com.ecom2.product.dto.ProductDTO;
import com.ecom2.user.entity.User;
import com.ecom2.user.service.UserService;
import jakarta.transaction.Transactional;
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

        Product product = productService.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", productId));

        if (product.getStockQuantity() == 0) {
            throw new APIException(product.getName() + " is not available");
        }

        if (product.getStockQuantity() < quantity) {
            throw new APIException("Please, make an order of the " + product.getName()
                    + " less than or equal to the quantity " + product.getStockQuantity());
        }

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

//        product.setStockQuantity(product.getStockQuantity() - quantity);
//        productService.saveProduct(product);

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
                            .map(p -> modelMapper.map(p.getProduct(), ProductDTO.class))
                            .collect(Collectors.toList());

                    cartDTO.setProducts(productDTOS);
                    return cartDTO;
                })
                .collect(Collectors.toList());

        return cartDTOS;
    }

    @Transactional
    public void deleteCartItemByProductIdAndCartId(Long cartId, Long productId) {

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartId));

        CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartId, productId);

        if(cartItem == null){
            throw new ResourceNotFoundException("Product", "productId", productId);
        }

        cart.setTotalPrice(cart.getTotalPrice() - (cartItem.getProductPrice() * cartItem.getQuantity()));
        cartRepository.save(cart);

        cartItemRepository.deleteByCart_CartIdAndProduct_ProductId(cartId, productId);

    }

    @Override
    public CartItemDTO updateProductQuantityInCart(CartItemDTO cartItemDTO) {
        Cart cart = cartRepository.findById(cartItemDTO.getCartId())
                .orElseThrow(() -> new ResourceNotFoundException("Cart", "cartId", cartItemDTO.getCartId()));

        Product product = productService.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", cartItemDTO.getProductId()));

        if(product.getStockQuantity() == 0){
            throw new APIException(product.getName() + "is not available");
        }

        if(product.getStockQuantity() < cartItemDTO.getQuantity()){
            throw new APIException("Please, make an order of the " + product.getName()
            + " less than or equal to the quantity " + product.getStockQuantity());
        }

        CartItem cartItem = cartItemRepository.findByCart_CartIdAndProduct_ProductId(cartItemDTO.getCartId(), cartItemDTO.getProductId());

        if(cartItem == null){
            throw new APIException("Product " + product.getName() + " not available in the cart!" );
        }

        cart.setTotalPrice(cart.getTotalPrice() + cartItem.getProductPrice() *  (cartItemDTO.getQuantity() - cartItem.getQuantity()));
        cartRepository.save(cart);
        cartItem.setQuantity(cartItemDTO.getQuantity());
        cartItemRepository.save(cartItem);

        CartItemDTO newCartItemDTO = new CartItemDTO();
        newCartItemDTO.setCartId(cartItemDTO.getCartId());
        newCartItemDTO.setProductId(cartItemDTO.getProductId());
        newCartItemDTO.setQuantity(cartItemDTO.getQuantity());

        return newCartItemDTO;
    }

}
