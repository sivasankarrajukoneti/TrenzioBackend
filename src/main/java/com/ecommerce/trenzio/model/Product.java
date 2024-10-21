package com.ecommerce.trenzio.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@ToString
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @NotBlank
    @Size(min = 3, message = "Product Name Must Contain At Least 3 Characters")
    @Column(name = "product_name")
    private String productName;

    // URL or path to the product image
    private String image;

    @NotBlank
    @Size(min = 6, message = "Product Description Must Contain At Least 6 Characters")
    @Column(name = "description")
    private String description;


    @Min(value = 0, message = "Stocks cannot be negative")
    private Integer stocks;

    @DecimalMin(value = "0.0", inclusive = true, message = "Price cannot be negative")
    private double price;

    @DecimalMin(value = "0.0", inclusive = true, message = "Discount cannot be negative")
    private double discount;

    @DecimalMin(value = "0.0", inclusive = true, message = "Special price cannot be negative")
    private double specialPrice;

    // SKU (Stock Keeping Unit) for inventory tracking
    @Column(unique = true)
    private String sku;

    // Brand or manufacturer of the product
    private String brand;

    // Average rating of the product
    private Double ratings;

    // Date and time the product was added to the catalog
    private LocalDateTime createdAt = LocalDateTime.now();

    // Date and time the product was last updated
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Many-to-One relationship with Category
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    // Many-to-One relationship with User (seller)
    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User user;

    // One-to-Many relationship with CartItem
    @OneToMany(mappedBy = "product", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<CartItem> cartItems = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<OrderItem> orderItems = new ArrayList<>();

    // Automatically set createdAt and updatedAt before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Automatically update the updatedAt field before any update
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
