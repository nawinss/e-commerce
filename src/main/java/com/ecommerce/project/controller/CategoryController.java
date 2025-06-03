package com.ecommerce.project.controller;


import com.ecommerce.project.payload.CategoryDTO;
import com.ecommerce.project.payload.CategoryResponse;
import com.ecommerce.project.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
public class CategoryController {
     @Autowired
    CategoryService categoryService;

     @GetMapping("/api/public/categories")
    public ResponseEntity<CategoryResponse> getAllCategories(@RequestParam(name= "pageNumber", defaultValue = "0") @Valid Integer pageNumber,
                                                             @RequestParam(name= "pageSize", defaultValue = "10") @Valid Integer pageSize,
                                                             @RequestParam(name= "sortBy") @Valid String sortBy,
                                                             @RequestParam(name= "sortOrder") @Valid String sortOrder){
        return new ResponseEntity<>(categoryService.getAllCategories(pageNumber,pageSize,sortBy,sortOrder), HttpStatus.OK);
    }
    @PostMapping("/api/public/categories")
    public ResponseEntity<String> addCategory(@Valid @RequestBody CategoryDTO newCategory){
        CategoryDTO createdNewCategory = categoryService.createCategory(newCategory);
        return ResponseEntity.ok("Category with name "+ createdNewCategory.getCategoryName()+ " created successfully");
    }
    @DeleteMapping("/api/admin/categories")
    public ResponseEntity<CategoryDTO> deleteCategory(@RequestParam Long categoryId){
         
        CategoryDTO deletedCategoryDTO = categoryService.deleteCategory(categoryId);
         return new ResponseEntity<>(deletedCategoryDTO, HttpStatus.OK);

    }

    @GetMapping("/api/public/categories/category")
    public ResponseEntity<CategoryDTO> getCategoryById(@RequestParam Long categoryId){
        CategoryDTO fetchedCategory = categoryService.getCategoryById(categoryId);
            return ResponseEntity.ok(fetchedCategory);
    }

    @RequestMapping(value="/api/public/categories/{categoryId}", method = RequestMethod.PUT)
    public ResponseEntity<CategoryDTO> updateCategoryById(@PathVariable Long categoryId, @Valid @RequestBody CategoryDTO newCategory){
        CategoryDTO updatedCategory = categoryService.updateCategory(categoryId,newCategory);
             return ResponseEntity.status(200).body("Updated Category details = ").ok(updatedCategory);
    }

}
