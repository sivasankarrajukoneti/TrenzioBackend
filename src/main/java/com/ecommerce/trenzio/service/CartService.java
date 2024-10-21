package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.CartDTO;

public interface CartService {

    // Create a new cart for a given user
    CartDTO createCart(Long userId);

    // Get a cart by its ID
    CartDTO getCartById(Long cartId);

    // Update cart's total price and other information
    CartDTO updateCart(Long cartId, CartDTO cartDTO);

    // Delete a cart by its ID
    void deleteCart(Long cartId);

    // Clear all items in the cart
    CartDTO clearCart(Long cartId);
}
