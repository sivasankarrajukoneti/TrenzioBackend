package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {
    private Long cartItemId;
    private Long productId;    // Simplified reference to product ID
    private String productName; // Product name for display purposes
    private Integer quantity;
    private Double productPrice;
    private Double discount;
    private Double subTotal;

    // Constructor for convenience (mapping from entity to DTO)
    public CartItemDTO(Long cartItemId, Long productId, String productName, Integer quantity, Double productPrice, Double discount) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.quantity = quantity;
        this.productPrice = productPrice;
        this.discount = discount;
        this.subTotal = (productPrice - discount) * quantity; // Calculate subtotal
    }
}
