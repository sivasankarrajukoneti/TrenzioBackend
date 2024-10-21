package com.ecommerce.trenzio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long paymentId;

    // Payment amount
    @Column(nullable = false)
    private Double amount;

    // Enum for payment method (e.g., CREDIT_CARD, DEBIT_CARD, PAYPAL, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    // Enum for payment status (e.g., PENDING, PAID, FAILED, etc.)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus paymentStatus;

    // One-to-One relationship with Order
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Many-to-One relationship with User (Buyer)
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // Timestamp for payment creation
    private LocalDateTime createdAt = LocalDateTime.now();

    // Timestamp for last update
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Automatically update timestamps
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
