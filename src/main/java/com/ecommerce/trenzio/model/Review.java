package com.ecommerce.trenzio.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "reviews")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with User
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Many-to-One relationship with Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Rating for the product (1-5)
    @Column(nullable = false)
    private Integer rating;

    // Review text provided by the user
    @Column(length = 1000)
    private String reviewText;

    // Timestamp for when the review was created
    private LocalDateTime createdAt = LocalDateTime.now();
}
