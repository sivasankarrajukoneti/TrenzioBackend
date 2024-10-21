package com.ecommerce.trenzio.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Many-to-One relationship with Order
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    @JsonBackReference  // Prevent circular reference for JSON serialization
    @EqualsAndHashCode.Exclude
    private Order order;

    // Many-to-One relationship with Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Store product price at the time of order
    @Column(nullable = false)
    private Double productPrice; // Store snapshot of product price

    // Quantity of product in the order
    @Column(nullable = false)
    private Integer quantity;

    // Price at the time of order
    @Column(nullable = false)
    private Double price;

    // Discount applied to this product in the order
    private Double discount = 0.0;


    // Persisted field for subtotal
    @Column(nullable = false)
    private Double subTotal;

    // Audit fields
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Automatically update the `subTotal` field and `updatedAt` before persisting
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.subTotal = calculateSubTotal();  // Calculate subtotal
    }

    // Automatically update the `subTotal` field and `updatedAt` before any update
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
        this.subTotal = calculateSubTotal();  // Recalculate subtotal in case of changes
    }

    // Calculate the subtotal for the cart item (price - discount) * quantity
    public Double calculateSubTotal() {
        return (price - discount) * quantity;
    }
}
