package com.ecommerce.project.service;

import com.ecommerce.project.exceptionHandler.APIException;
import com.ecommerce.project.exceptionHandler.ResourceNotFoundException;
import com.ecommerce.project.model.Category;
import com.ecommerce.project.model.Product;
import com.ecommerce.project.payload.ProductDTO;
import com.ecommerce.project.payload.ProductResponse;
import com.ecommerce.project.repository.CategoryRepository;
import com.ecommerce.project.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private static final String PRODUCT = "Product";
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;

    @Value("${property.imagePath}")
    String path;


    @Override
    public ProductResponse getAllProducts(Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPages = productRepository.findAll(pageDetails);

        List<Product> allProducts = productPages.getContent();

//        List<Product> allProducts = productRepository.findAll();


        if(allProducts.isEmpty())
            throw new APIException("Products list is empty");

        List<ProductDTO> productDTOS = allProducts.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());

        return productResponse;
    }

    @Override
    public ProductDTO addProduct(Long categoryId, ProductDTO productDTO) {

       Category category = categoryRepository.findById(categoryId)
                            .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

       boolean isProductNotPresent = true;

       List<Product> Products = category.getProducts();

       for(Product product:Products){
           if(productDTO.getProductName().equals(product.getProductName())){
               isProductNotPresent = false;
               break;
           }
       }
        Product productToSave;

       if(isProductNotPresent){
           productDTO.setImage("default.png");
           productDTO.setCategory(category);
           double specialPrice = productDTO.getPrice() - ((productDTO.getDiscount() *0.01) * productDTO.getPrice());
           productDTO.setSpecialPrice(specialPrice);

            productToSave = modelMapper.map(productDTO, Product.class);
           productRepository.save(productToSave);

       }else{
           throw new APIException("Product with same name already present in this Category");
       }

       return modelMapper.map(productToSave, ProductDTO.class);

    }

    @Override
    public ProductDTO deleteProduct(Long productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        productRepository.delete(product);

        return productDTO;
    }

    @Override
    public ProductResponse getAllProductsByCategory(Long categoryId,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(()-> new ResourceNotFoundException("Category","categoryId",categoryId));

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPages = productRepository.findByCategoryOrderByPrice(category,pageDetails);

        List<Product> productsByCategory = productPages.getContent();

        if(productsByCategory.isEmpty())
            throw new APIException("Products list is empty");

        List<ProductDTO> productDTOS = productsByCategory.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setContent(productDTOS);
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());
        return productResponse;
    }

    @Override
    public ProductResponse getAllProductsByKeyword(String keyword,Integer pageNumber,Integer pageSize,String sortBy,String sortOrder) {

        Sort sort = sortOrder.equalsIgnoreCase(Sort.Direction.ASC.name())
                ? Sort.by(sortBy).ascending()
                : Sort.by(sortBy).descending();
        Pageable pageDetails = PageRequest.of(pageNumber,pageSize,sort);

        Page<Product> productPages = productRepository.findByProductNameIsLikeIgnoreCase("%"+keyword+"%",pageDetails);

        List<Product> productsByKeyword = productPages.getContent();

        if(productsByKeyword.isEmpty())
            throw new APIException("Products list with keyword "+ keyword+ "is empty");

        List<ProductDTO> productDTOS = productsByKeyword.stream().map(product->modelMapper.map(product,ProductDTO.class)).toList();
        ProductResponse productResponse = new ProductResponse();
        productResponse.setPageNumber(productPages.getNumber());
        productResponse.setPageSize(productPages.getSize());
        productResponse.setContent(productDTOS);
        productResponse.setTotalPages(productPages.getTotalPages());
        productResponse.setLastPage(productPages.isLast());
        return productResponse;

    }

    @Override
    public ProductDTO updateProduct(Long productId, ProductDTO productDTO) {

        Product product = productRepository.findById(productId)
                        .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        ProductDTO productDTOInDB = modelMapper.map(product,ProductDTO.class);

        productDTOInDB.setProductName(productDTO.getProductName());
        productDTOInDB.setPrice(productDTO.getPrice());
        productDTOInDB.setDiscount(productDTO.getDiscount());
        productDTOInDB.setCategory(productDTO.getCategory());
        productDTOInDB.setSpecialPrice(productDTO.getSpecialPrice());
        productDTOInDB.setDescription(productDTO.getDescription());

        Product newProduct = modelMapper.map(productDTOInDB, Product.class);
        productRepository.save(newProduct);

        return modelMapper.map(newProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO updateProductImage(Long productId, MultipartFile image) throws IOException {

        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ResourceNotFoundException("Product","productId",productId));

        ProductDTO productDTOInDB = modelMapper.map(product,ProductDTO.class);

        String fileName = fileService.uploadImage(path,image);
        productDTOInDB.setImage(fileName);

        Product newProduct = modelMapper.map(productDTOInDB, Product.class);
        productRepository.save(newProduct);

        return modelMapper.map(newProduct, ProductDTO.class);
    }
}
