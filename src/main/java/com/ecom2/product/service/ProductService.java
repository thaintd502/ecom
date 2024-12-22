package com.ecom2.product.service;

import com.ecom2.product.dto.PageResponse;
import com.ecom2.product.dto.ProductDTO;
import com.ecom2.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductService {

//    List<ProductDTO> getAllProducts();
    PageResponse<List<ProductDTO>> getAllProducts(Pageable pageable);
//    void addProduct(ProductDTO productAddDTO);
    void saveProduct(Product product);
    Optional<Product> findById(Long productId);
    void deleteProduct(Long productId);
    List<Product> getProductsByCategoryId(Long categoryId);
    List<Product> searchProductsByKeyword(String keyword);
    List<Product> getDiscountedProducts();
}
