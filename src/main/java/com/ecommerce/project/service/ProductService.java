package com.ecommerce.project.service;

import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ProductService {

    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);
    public ProductDTO addProduct(Long categoryId,ProductDTO productDTO);
    public ProductDTO deleteProduct(Long productId);

    public ProductResponse getAllProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    public ProductResponse getAllProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder);

    public ProductDTO updateProduct(Long productId, ProductDTO productDTO);

    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException;
}
