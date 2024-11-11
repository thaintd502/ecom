package com.ecom2.product;

import com.ecom2.brand.Brand;
import com.ecom2.brand.BrandRepository;
import com.ecom2.category.Category;
import com.ecom2.category.CategoryRepository;
import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.product.dto.ProductDTO;
import org.modelmapper.ModelMapper;
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

    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        List<ProductDTO> productDTOS = products.stream().
                map(p -> modelMapper.map(p, ProductDTO.class))
                .collect(Collectors.toList());
        return productDTOS;
    }

//    public void addProduct(ProductDTO productAddDTO) {
//        Product product = new Product();
//
//        product.setProductCode(productAddDTO.getProductCode());
//        product.setName(productAddDTO.getName());
//        product.setPrice(productAddDTO.getPrice());
//        product.setPromotePrice(productAddDTO.getPromotePrice());
//        product.setStockQuantity(productAddDTO.getImportQuantity());
//        product.setImportPrice(productAddDTO.getImportPrice());
//        product.setDescription(productAddDTO.getDescription());
//        product.setImageUrl(productAddDTO.getImageUrl());
//
//        // Fetch and set the Brand by its ID
//        Brand brand = brandRepository.findById(Long.parseLong(productAddDTO.getBrand()))
//                .orElseThrow(() -> new IllegalArgumentException("Invalid brand ID: " + productAddDTO.getBrand()));
//        product.setBrand(brand);
//
//        // Fetch and set the Categories (assuming multiple categories are allowed)
//        List<Long> categoryIds = Arrays.stream(productAddDTO.getCategory().split(","))
//                .map(Long::parseLong)
//                .collect(Collectors.toList());
//        Set<Category> categories = new HashSet<>(categoryRepository.findAllById(categoryIds));
//        product.setCategories(categories);
//
//        // Save the product to the database
//        productRepository.save(product);
//    }

    @Override
    public void saveProduct(Product product) {
        productRepository.save(product);
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return productRepository.findById(productId);
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepository.findProductsByCategoryId(categoryId);
    }

    @Override
    public List<Product> searchProductsByKeyword(String keyword) {
        return productRepository.searchProductsByKeyword(keyword);
    }

    @Override
    public List<Product> getDiscountedProducts() {
        return productRepository.findByDiscountGreaterThan(0);
    }


}
