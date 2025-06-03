package com.ecommerce.project.service;

import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.repository.CategoryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

@Autowired
private CategoryRepository categoryRepository;
private static final String CATEGORY = "Category";
@Autowired
private ModelMapper modelMapper;

    @Override
    public CategoryResponse getAllCategories(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

       Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sort);

        Page<Category> categoryPage = categoryRepository.findAll(pageDetails);

        List<Category> categories = categoryPage.getContent();

        if (categories.isEmpty()) {
            throw new APIException("No Category created yet");
        }
        List<CategoryDTO> categoryDTOS = categories.stream().map(category->modelMapper.map(category,CategoryDTO.class)).toList();
        CategoryResponse categoryResponse = new CategoryResponse();

        categoryResponse.setContent(categoryDTOS);
        categoryResponse.setPageNumber(pageNumber);
        categoryResponse.setPageSize(pageSize);
        categoryResponse.setTotalPages(categoryPage.getTotalPages());
        categoryResponse.setLastPage(categoryPage.isLast());
        categoryResponse.setSortBy(String.valueOf(sortBy));
        categoryResponse.setSortOrder(sortOrder);

        return categoryResponse;
    }

    @Override
    public CategoryDTO getCategoryById(Long categoryId) {
    Category category = new Category();
        category=categoryRepository
                .findById(categoryId)
                .orElseThrow(()->new ResourceNotFoundException(CATEGORY,"categoryId",categoryId));
        CategoryDTO categoryDTO= modelMapper.map(category,CategoryDTO.class);
        return categoryDTO;
    }

    @Override
    public CategoryDTO createCategory(CategoryDTO newCategoryDTO) {
        Category category = modelMapper.map(newCategoryDTO, Category.class);
        Category categoryFromDB = categoryRepository.findByCategoryName(category.getCategoryName());

        if (categoryFromDB!= null) {
            throw new APIException("Category already present pls give another name");
        }

        Category  savedCategory= categoryRepository.save(category);

        return modelMapper.map(savedCategory,CategoryDTO.class);
    }

    @Override
    public CategoryDTO updateCategory(Long categoryId,CategoryDTO categoryDTOToUpdate) {

       Category category = categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(CATEGORY,"categoryId",categoryId));
       Category updatedCategory = modelMapper.map(categoryDTOToUpdate,Category.class);
        updatedCategory.setCategoryId(categoryId);

       categoryRepository.save(updatedCategory);
       CategoryDTO updatedCategoryDTO = modelMapper.map(updatedCategory,CategoryDTO.class);

           return updatedCategoryDTO;

    }

    @Override
    public CategoryDTO deleteCategory(Long categoryId) {

        Category categoryTobeDeleted =categoryRepository.findById(categoryId).orElseThrow(()->new ResourceNotFoundException(CATEGORY,"categoryId",categoryId));
        categoryRepository.deleteById(categoryTobeDeleted.getCategoryId());
        return modelMapper.map(categoryTobeDeleted,CategoryDTO.class);
    }
}
