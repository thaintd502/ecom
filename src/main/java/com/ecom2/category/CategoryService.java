package com.ecom2.category;


import java.io.IOException;
import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories();
    Category findByName(String name);
    Category addCategory(CategoryDTO categoryDTO) throws IOException;
    Category editCategory(Long categoryId, CategoryDTO categoryDTO) throws IOException;
    void deleteCategory(Long categoryId);
}
