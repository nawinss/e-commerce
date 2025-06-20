package com.ecommerce.project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private List<ProductDTO> content;

    private Integer pageNumber;
    private Integer pageSize;
    private Integer totalPages;
    private boolean lastPage;
    private String sortBy;
    private String sortOrder;
}
