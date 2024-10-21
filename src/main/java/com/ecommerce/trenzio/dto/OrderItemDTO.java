package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
    private Long orderItemId;
    private Long productId;     // Simplified reference to product ID
    private String productName; // Product name for display purposes
    private Integer quantity;
    private Double price;
    private Double discount;
    private Double subTotal;

    // Constructor for convenience (mapping from entity to DTO)
    public OrderItemDTO(Long orderItemId, Long productId, String productName, Integer quantity, Double price, Double discount) {
        this.orderItemId = orderItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
        this.discount = discount;
        this.subTotal = calculateSubTotal(); // Calculate subtotal
    }

    // Helper method to calculate subtotal
    public Double calculateSubTotal() {
        return (price - discount) * quantity;
    }
}