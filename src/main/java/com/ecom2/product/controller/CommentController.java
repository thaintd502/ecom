package com.ecom2.product.controller;

import com.ecom2.auth.jwt.JwtTokenProvider;
import com.ecom2.product.dto.CommentDTO;
import com.ecom2.product.entity.Comment;
import com.ecom2.product.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public")
public class CommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @GetMapping("/average-rating/{productId}")
    public ResponseEntity<?> getAverageRating(@PathVariable Long productId){
        Double averageRating = commentService.getAverageRatingForProduct(productId);
        return ResponseEntity.status(200).body(averageRating);
    }

    @PostMapping("/add-comment")
    public ResponseEntity<?> addComment(@RequestHeader("Authorization") String token,
                                        @RequestBody CommentDTO commentDTO){
        try{
            String userName = jwtTokenProvider.getUserNameFromJwt(token.substring(7));
            Comment comment = commentService.addComment(userName, commentDTO);
            return ResponseEntity.status(200).body(comment);
        }catch (Exception e){
            return ResponseEntity.status(400).body("Error: " + e.getMessage());
        }
    }
}
