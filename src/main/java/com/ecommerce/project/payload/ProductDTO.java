package com.ecommerce.project.payload;

import com.ecommerce.project.model.Category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private long productId;
    private String productName;
    private String description;
//    @NotBlank
    private double price;
    private double discount;
    private double specialPrice;
//    @NotBlank
    private int quantity;
    private String image;

    private Category category;
}
