package com.ecom2.category;


import java.util.List;

public interface CategoryService {
    public List<Category> getAllCategories();
    Category findByName(String name);
}
