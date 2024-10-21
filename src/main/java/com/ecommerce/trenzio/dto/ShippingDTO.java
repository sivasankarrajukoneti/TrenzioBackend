package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShippingDTO {
    private Long id;
    private Long orderId;  // Simplified reference to the Order entity
    private String shippingMethod;
    private String trackingNumber;
    private LocalDateTime shippingDate;
    private LocalDateTime estimatedDeliveryDate;
    private LocalDateTime deliveryDate;
}
