package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long productId;
    private String productName;
    private String image;
    private String description;
    private Integer stocks;
    private double price;
    private double discount;
    private double specialPrice;
    private String sku;
    private String brand;
    private Double ratings;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long categoryId;  // Simplified category reference
    private Long sellerId;    // Simplified seller reference
}
