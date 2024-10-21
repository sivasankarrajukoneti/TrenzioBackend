package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.CartDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.CartMapper;
import com.ecommerce.trenzio.model.Cart;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.repository.CartRepository;
import com.ecommerce.trenzio.repository.UserRepository;
import com.ecommerce.trenzio.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor  // Lombok annotation to auto-generate constructor for final fields
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartMapper cartMapper;

    @Override
    @Transactional
    public CartDTO createCart(Long userId) {
        // Retrieve the user associated with the cart
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        // Create a new Cart and associate it with the user
        Cart cart = new Cart();
        cart.setUser(user);

        // Save the new Cart
        Cart savedCart = cartRepository.save(cart);

        return cartMapper.cartToDTO(savedCart);
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO getCartById(Long cartId) {
        // Retrieve the cart by its ID
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        return cartMapper.cartToDTO(cart);
    }

    @Override
    @Transactional
    public CartDTO updateCart(Long cartId, CartDTO cartDTO) {
        // Retrieve the existing cart
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        // Update the cart's total price (you can add more fields to update as needed)
        existingCart.setTotalPrice(cartDTO.getTotalPrice());

        // Save the updated cart
        Cart updatedCart = cartRepository.save(existingCart);

        return cartMapper.cartToDTO(updatedCart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        // Check if the cart exists before deleting
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        cartRepository.deleteById(cartId);
    }

    @Override
    @Transactional
    public CartDTO clearCart(Long cartId) {
        // Retrieve the existing cart
        Cart existingCart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        // Clear all items from the cart
        existingCart.getCartItems().clear();
        existingCart.setTotalPrice(0.0);

        // Save the cleared cart
        Cart clearedCart = cartRepository.save(existingCart);

        return cartMapper.cartToDTO(clearedCart);
    }
}
