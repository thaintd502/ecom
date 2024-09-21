package com.ecom2.product;

import com.ecom2.brand.Brand;
import com.ecom2.brand.BrandRepository;
import com.ecom2.category.Category;
import com.ecom2.category.CategoryRepository;
import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.product.dto.ProductAddDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    CloudinaryService cloudinaryService;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public void addProduct(ProductAddDTO productAddDTO) {
        Product product = new Product();

        // Setting basic product fields
        product.setProductCode(productAddDTO.getProductCode());
        product.setName(productAddDTO.getProductName());
        product.setPrice(productAddDTO.getPrice());
        product.setPromotePrice(productAddDTO.getPromotePrice());
        product.setStockQuantity(productAddDTO.getImportQuantity());
        product.setImportPrice(productAddDTO.getImportPrice());
        product.setDescription(productAddDTO.getDescription());
        product.setImageUrl(productAddDTO.getImageUrl());

        // Fetch and set the Brand by its ID
        Brand brand = brandRepository.findById(Long.parseLong(productAddDTO.getBrand()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID: " + productAddDTO.getBrand()));
        product.setBrand(brand);

        // Fetch and set the Categories (assuming multiple categories are allowed)
        List<Long> categoryIds = Arrays.stream(productAddDTO.getCategory().split(","))
                .map(Long::parseLong)
                .collect(Collectors.toList());
        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
        product.setCategories(categories);

        // Save the product to the database
        productRepository.save(product);
    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }


}
