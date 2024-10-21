package com.ecommerce.trenzio.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "carts")
@NoArgsConstructor
@AllArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    // One-to-One relationship with User
    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // One-to-Many relationship with CartItem, ensuring cascade operations and orphan removal
    @OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    private List<CartItem> cartItems = new ArrayList<>();

    // Total price of items in the cart
    private Double totalPrice = 0.0;

    // Calculate the total price based on cart items
    public void calculateTotalPrice() {
        this.totalPrice = cartItems.stream()
                .mapToDouble(CartItem::calculateSubTotal)
                .sum();
    }
}
