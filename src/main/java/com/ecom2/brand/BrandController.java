package com.ecom2.brand;

import com.ecom2.category.Category;
import com.ecom2.category.CategoryDTO;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class BrandController {

    @Autowired
    private BrandService brandService;

    @GetMapping("/public/get-all-brands")
    public ResponseEntity<List<Brand>> getAllBrands() {
        List<Brand> brands = brandService.getAllBrands();
        return ResponseEntity.ok(brands);
    }

    @PostMapping("/admin/add-brand")
    public ResponseEntity<?> addBrand(@Valid @RequestBody BrandDTO brandDTO){
        try{
            Brand brand = brandService.addBrand(brandDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(brand);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @PutMapping("/admin/edit-brand/{brandId}")
    public ResponseEntity<?> editBrand(@PathVariable Long brandId,
                                       @Valid @RequestBody BrandDTO brandDTO){
        try{
            Brand brand = brandService.editBrand(brandId, brandDTO);
            return ResponseEntity.status(HttpStatus.OK).body(brand);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/admin/delete-brand/{brandId}")
    public ResponseEntity<?> deleteBrand(@PathVariable Long brandId){
        try{
            brandService.deleteBrand(brandId);
            return ResponseEntity.ok("Brand deleted successfully!");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Brand not found: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}
