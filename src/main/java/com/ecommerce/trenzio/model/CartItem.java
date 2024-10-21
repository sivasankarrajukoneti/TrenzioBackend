package com.ecommerce.trenzio.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    // Many-to-One relationship with Cart
    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // Many-to-One relationship with Product
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // Quantity of the product in the cart
    @Column(nullable = false)
    private Integer quantity;

    // Discount applied to the product (can be a percentage or fixed amount)
    @Column(nullable = false)
    private Double discount = 0.0;

    // Product price at the time it was added to the cart
    @Column(nullable = false)
    private Double productPrice;


    // Subtotal for this cart item
    @Column(nullable = false)
    private Double subtotal;

    // Calculate the subtotal for the cart item (product price - discount) * quantity
    public Double calculateSubTotal() {
        return (productPrice - discount) * quantity;
    }

    // Automatically update the subtotal before persisting or updating
    @PrePersist
    @PreUpdate
    protected void updateSubtotal() {
        this.subtotal = calculateSubTotal();
    }

}
