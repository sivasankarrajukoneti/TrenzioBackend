package com.ecommerce.trenzio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;  // Updated column name

    @Column(nullable = false)
    private String name;

    private String description;

    private String imageUrl;

    // Many-to-One relationship for hierarchical categories (self-referencing)
    @ManyToOne
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    // One-to-Many relationship for subcategories (self-referencing)
    @OneToMany(mappedBy = "parentCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Category> subcategories = new ArrayList<>();

    // One-to-Many relationship with Product
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean isActive = true;

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
