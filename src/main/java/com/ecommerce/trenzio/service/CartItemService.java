package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.CartItemDTO;

import java.util.List;

public interface CartItemService {

    // Method to add a new CartItem to a Cart
    CartItemDTO addCartItem(Long cartId, CartItemDTO cartItemDTO);

    // Method to update an existing CartItem
    CartItemDTO updateCartItem(Long cartId, Long cartItemId, CartItemDTO cartItemDTO);

    // Method to delete a CartItem
    void deleteCartItem(Long cartId, Long cartItemId);

    // Method to get all CartItems for a given Cart
    List<CartItemDTO> getAllCartItems(Long cartId);

    // Method to get a specific CartItem by its ID
    CartItemDTO getCartItemById(Long cartItemId);
}
