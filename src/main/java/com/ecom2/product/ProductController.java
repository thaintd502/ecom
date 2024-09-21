package com.ecom2.product;

import com.ecom2.brand.Brand;
import com.ecom2.brand.BrandService;
import com.ecom2.category.Category;
import com.ecom2.category.CategoryService;
import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.product.dto.ProductAddDTO;
import com.ecom2.product.dto.ProductTableDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;


    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get-all-products")
    public ResponseEntity<List<ProductTableDTO>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        List<ProductTableDTO> productDTOs = products.stream().map(this::convertToProductTableDTO).collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    private ProductTableDTO convertToProductTableDTO(Product product) {
        ProductTableDTO productDTO = new ProductTableDTO();
        productDTO.setProductId(product.getProductId());
        productDTO.setName(product.getName());
        productDTO.setCategory(product.getCategories().stream().map(Category::getName).collect(Collectors.joining(", ")));
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }

    @GetMapping("/product/{id}")
    public ResponseEntity<Optional<Product>> getProductById(@PathVariable Long id){
        return ResponseEntity.ok(productService.findById(id));
    }

    @PostMapping("/add-product")
    public ResponseEntity<?> addProduct(
            @RequestParam(required = false) String productCode,
            @RequestParam(required = false) String productName,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String importPrice,
            @RequestParam(required = false) String price,
            @RequestParam(required = false) String promotePrice,
            @RequestParam(required = false) MultipartFile imageUrl,
            @RequestParam(required = false) String importQuantity,
            @RequestParam(required = false) String description) throws IOException, ParseException
           {
        try {

            Product product =  new Product();
            product.setProductCode(productCode);
            product.setName(productName);
            product.setImportPrice(Double.parseDouble(importPrice));
            product.setPrice(Double.parseDouble(price));
            product.setPromotePrice(Double.parseDouble(promotePrice));
            product.setImportQuantity(Integer.parseInt(importQuantity));
            product.setDescription(description);
            product.setImageUrl(cloudinaryService.uploadFile(imageUrl));

            Brand brandSave = brandService.findByName(brand);
            product.setBrand(brandSave);

            Set<Category> cate = new HashSet<>();
            cate.add(categoryService.findByName(category));
            product.setCategories(cate);

            productService.saveProduct(product);

            return ResponseEntity.ok("Product added successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
        }
    }

}
