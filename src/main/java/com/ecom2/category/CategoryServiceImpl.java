package com.ecom2.category;

import com.ecom2.cloudinary.CloudinaryService;
import com.ecom2.exception.APIException;
import com.ecom2.exception.ResourceNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService{

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Category addCategory(CategoryDTO categoryDTO) throws IOException {

        if (categoryRepository.existsByName(categoryDTO.getName())) {
            throw new APIException("Category with the same name already exists!");
        }
        String imageUrl = "";
        if(categoryDTO.getImageUrl() != null && !categoryDTO.getImageUrl().isEmpty()){
            imageUrl = cloudinaryService.uploadFile(categoryDTO.getImageUrl());
        }
        Category category = Category.builder()
                .name(categoryDTO.getName())
                .description(categoryDTO.getDescription())
                .imageUrl(imageUrl)
                .build();
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category editCategory(Long categoryId, CategoryDTO categoryDTO) throws IOException {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category", "categoryId", categoryId));
        String imageUrl = "";
        if(categoryDTO.getImageUrl() != null && !categoryDTO.getImageUrl().isEmpty()){
            imageUrl = cloudinaryService.uploadFile(categoryDTO.getImageUrl());
            category.setImageUrl(imageUrl);
        }
        category.setName(categoryDTO.getName());
        category.setDescription(categoryDTO.getDescription());
        categoryRepository.save(category);
        return category;
    }

    @Override
    public void deleteCategory(Long categoryId) {
        categoryRepository.deleteById(categoryId);
    }


}
