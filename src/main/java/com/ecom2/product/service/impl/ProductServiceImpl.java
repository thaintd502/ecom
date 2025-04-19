package com.ecom2.product.service.impl;

import com.ecom2.brand.BrandRepository;
import com.ecom2.category.CategoryRepository;
import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.product.dto.PageResponse;
import com.ecom2.product.dto.ProductDTO;
import com.ecom2.product.entity.Product;
import com.ecom2.product.repository.ProductRepository;
import com.ecom2.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
//@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private BrandRepository brandRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    CloudinaryService cloudinaryService;


    private final ModelMapper modelMapper;

    private final RedisTemplate redisTemplate;
    @Autowired
    public ProductServiceImpl(ModelMapper modelMapper, RedisTemplate redisTemplate) {
        this.modelMapper = modelMapper;
        this.redisTemplate = redisTemplate;
    }

    public PageResponse<List<ProductDTO>> getAllProducts(Pageable pageable) {
        String redisKey = "products:page:" + pageable.getPageNumber() + ":size:" + pageable.getPageSize();

        PageResponse<List<ProductDTO>> cached = (PageResponse<List<ProductDTO>>) redisTemplate.opsForValue().get(redisKey);
        if (cached != null) {
            return cached;
        }

        Page<Product> productPage = productRepository.findAll(pageable);
        List<ProductDTO> productDTOS = productPage.getContent().stream()
                .map(p -> modelMapper.map(p, ProductDTO.class))
                .toList();

        PageResponse<List<ProductDTO>> response = new PageResponse<>(
                productPage.getSize(),
                productPage.getNumber(),
                productPage.getTotalPages(),
                productDTOS
        );

        redisTemplate.opsForValue().set(redisKey, response, Duration.ofMinutes(5));
        return response;
    }



//    public PageResponse<List<ProductDTO>> getAllProducts(Pageable pageable) {
//
//        Page<Product> productPage = productRepository.findAll(pageable);
//        List<ProductDTO> productDTOS = productPage.getContent().stream()
//                .map(p -> modelMapper.map(p, ProductDTO.class))
//                .toList();
//
//        return new PageResponse<>(
//                productPage.getSize(),
//                productPage.getNumber(),
//                productPage.getTotalPages(),
//                productDTOS
//        );
//    }

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

    @Override
    public ProductDTO findProductById(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Sản phẩm không tồn tại"));
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        return productDTO;
    }


}
