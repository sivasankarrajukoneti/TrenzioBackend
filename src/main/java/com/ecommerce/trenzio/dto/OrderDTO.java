package com.ecommerce.trenzio.dto;

import com.ecommerce.trenzio.model.OrderStatus;
import com.ecommerce.trenzio.model.PaymentMethod;
import com.ecommerce.trenzio.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long orderId;
    private Long userId;  // Include the UserId to link to the User entity
    private LocalDateTime orderDate;
    private OrderStatus status;  // Use enum instead of String for status
    private Double totalAmount;
    private Double discount;
    private AddressDTO shippingAddress;
    private AddressDTO billingAddress;
    private List<OrderItemDTO> orderItems;  // List of Order Items
    private PaymentStatus paymentStatus;
    private PaymentMethod paymentMethod;
    private LocalDateTime shippingDate;
    private LocalDateTime deliveryDate;
    private String trackingNumber;
    private Boolean isCanceled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}