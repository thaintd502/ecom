package com.ecom2.product;

import com.ecom2.product.dto.ProductAddDTO;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    List<Product> getAllProducts();
    void addProduct(ProductAddDTO productAddDTO);
    void saveProduct(Product product);
    Optional<Product> findById(Long productId);
}
