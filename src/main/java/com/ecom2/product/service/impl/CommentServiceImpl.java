package com.ecom2.product.service.impl;

import com.ecom2.exception.APIException;
import com.ecom2.exception.ResourceNotFoundException;
import com.ecom2.order.repository.OrderRepository;
import com.ecom2.product.dto.CommentDTO;
import com.ecom2.product.entity.Comment;
import com.ecom2.product.entity.Product;
import com.ecom2.product.repository.CommentRepository;
import com.ecom2.product.repository.ProductRepository;
import com.ecom2.product.service.CommentService;
import com.ecom2.user.entity.User;
import com.ecom2.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Double getAverageRatingForProduct(Long productId) {
        return commentRepository.findAverageRatingByProductId(productId);
    }

    @Override
    @Transactional
    public Comment addComment(String userName, CommentDTO commentDTO) {

        Product product = productRepository.findById(commentDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product", "productId", commentDTO.getProductId()));

        User user = userRepository.findByUserName(userName);

        if(user == null){
            throw new APIException("User not found");
        }

        boolean hasPurchased = orderRepository.hasPurchasedProduct(userName, commentDTO.getProductId());

        if(!hasPurchased){
            throw new APIException("You can only comment on products you have purchased.");
        }

        Comment comment = Comment.builder()
                .product(product)
                .user(user)
                .content(commentDTO.getContent())
                .rating(commentDTO.getRating())
                .createdDate(new Date())
                .build();

        commentRepository.save(comment);

        return comment;
    }
}
