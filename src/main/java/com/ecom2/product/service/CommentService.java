package com.ecom2.product.service;

import com.ecom2.product.dto.CommentDTO;
import com.ecom2.product.entity.Comment;

public interface CommentService {
    Double getAverageRatingForProduct(Long productId);
    Comment addComment(String userName, CommentDTO commentDTO);
}
