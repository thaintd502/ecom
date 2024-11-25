package com.ecom2.product.repository;

import com.ecom2.product.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT AVG(c.rating) FROM Comment c WHERE c.product.productId = ?1")
    Double findAverageRatingByProductId(Long productId);
}
