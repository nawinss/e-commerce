package com.ecommerce.project.controller;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api")
public class ProductController {

   @Autowired
  ProductService productService;

   @GetMapping("/public/products")
   public ResponseEntity<ProductResponse> getAllProducts(@RequestParam(name= "pageNumber", defaultValue = "0") @Valid Integer pageNumber,
                                                         @RequestParam(name= "pageSize", defaultValue = "10") @Valid Integer pageSize,
                                                         @RequestParam(name= "sortBy") @Valid String sortBy,
                                                         @RequestParam(name= "sortOrder") @Valid String sortOrder){
       return ResponseEntity.status(200).body("Listing All products").ok(productService.getAllProducts(pageNumber,pageSize,sortBy,sortOrder));
   }

   @PostMapping ("admin/categories/{categoryId}/product")
   public ResponseEntity<ProductDTO> addProduct(@PathVariable Long categoryId, @Valid @RequestBody ProductDTO productDTO){
       return ResponseEntity.status(200).body("Product is added").ok( productService.addProduct(categoryId,productDTO));
   }

   @GetMapping("public/categories/{categoryId}/products")
    public ResponseEntity<ProductResponse> getAllProductsByCategory(@PathVariable Long categoryId,@RequestParam(name= "pageNumber", defaultValue = "0") @Valid Integer pageNumber,
                                                                    @RequestParam(name= "pageSize", defaultValue = "10") @Valid Integer pageSize,
                                                                    @RequestParam(name= "sortBy") @Valid String sortBy,
                                                                    @RequestParam(name= "sortOrder") @Valid String sortOrder){
       return ResponseEntity.ok(productService.getAllProductsByCategory(categoryId,pageNumber,pageSize,sortBy,sortOrder));
   }

   @GetMapping("public/products/keyword/{keyword}")
    public ResponseEntity<ProductResponse> getAllProductsByKeyword(@PathVariable String keyword,@RequestParam(name= "pageNumber", defaultValue = "0") @Valid Integer pageNumber,
                                                                   @RequestParam(name= "pageSize", defaultValue = "10") @Valid Integer pageSize,
                                                                   @RequestParam(name= "sortBy") @Valid String sortBy,
                                                                   @RequestParam(name= "sortOrder") @Valid String sortOrder){
       return ResponseEntity.ok(productService.getAllProductsByKeyword(keyword,pageNumber,pageSize,sortBy,sortOrder));
   }

   @PutMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long productId, @Valid @RequestBody ProductDTO productDTO){
       return ResponseEntity.ok(productService.updateProduct(productId,productDTO));
   }

   @DeleteMapping("admin/products/{productId}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long productId){
       return ResponseEntity.ok(productService.deleteProduct(productId));
   }

   @PutMapping("products/{productId}/image")
    public ResponseEntity<ProductDTO> updateProductImage(@PathVariable Long productId, @RequestParam MultipartFile image) throws IOException {
       return ResponseEntity.ok(productService.updateProductImage(productId,image));
   }
}
