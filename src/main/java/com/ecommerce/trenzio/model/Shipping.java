package com.ecommerce.trenzio.model;

import com.ecommerce.trenzio.util.TrackingNumberGenerator;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipping")
public class Shipping {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One-to-One relationship with Order
    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    // Enum for shipping method (e.g., Standard, Express, Overnight)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ShippingMethod shippingMethod;

    // Unique tracking number for the shipment
    @Column(unique = true, nullable = false)
    private String trackingNumber;

    private LocalDateTime shippingDate = LocalDateTime.now();

    private LocalDateTime estimatedDeliveryDate;

    private LocalDateTime deliveryDate;

    // Generate custom tracking number before persisting the entity
    @PrePersist
    public void prePersist() {
        this.trackingNumber = TrackingNumberGenerator.generateTrackingNumber();
    }
}
