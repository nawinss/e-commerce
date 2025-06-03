package com.ecommerce.project.service;

import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import org.springframework.stereotype.Service;

public interface CategoryService {
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    public CategoryDTO getCategoryById(Long id);
    public CategoryDTO createCategory(CategoryDTO newCategoryDTO);
    public CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTO);
    public CategoryDTO deleteCategory(Long categoryId);

}
