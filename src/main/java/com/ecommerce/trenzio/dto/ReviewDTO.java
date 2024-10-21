package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {
    private Long id;
    private Long userId;       // User who left the review, // Simplified reference to the User entity
    private Long productId;    // Reviewed product, // Simplified reference to the Product entity
    private Integer rating;    // Rating from 1 to 5
    private String reviewText; // Optional review text
    private LocalDateTime createdAt;
}
