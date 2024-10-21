package com.ecommerce.trenzio.dto;

import lombok.Data;

@Data
public class ProductSearchDTO {
    private String productName;
    private String category;
    private Double minPrice;
    private Double maxPrice;
    private String brand;
}
