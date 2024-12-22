package com.ecom2.category;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/public/get-all-categories")
    public ResponseEntity<List<Category>> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
        return ResponseEntity.ok(categories);
    }

    @PostMapping("/admin/add-category")
    public ResponseEntity<?> addCategory(@Valid @RequestBody CategoryDTO categoryDTO){
        try{
            Category category = categoryService.addCategory(categoryDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(category);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/admin/edit-category/{categoryId}")
    public ResponseEntity<?> editCategory(@PathVariable Long categoryId,
                                          @Valid @RequestBody CategoryDTO categoryDTO){
        try{
            Category category = categoryService.editCategory(categoryId, categoryDTO);
            return ResponseEntity.status(HttpStatus.OK).body(category);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
    @DeleteMapping("/admin/delete-category/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId){
        try{
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("Category deleted successfully!");
        }catch (EntityNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found: " + e.getMessage());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }
}
