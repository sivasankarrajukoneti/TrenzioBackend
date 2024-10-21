package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.CartItemDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.CartItemMapper;
import com.ecommerce.trenzio.model.Cart;
import com.ecommerce.trenzio.model.CartItem;
import com.ecommerce.trenzio.model.Product;
import com.ecommerce.trenzio.repository.CartItemRepository;
import com.ecommerce.trenzio.repository.CartRepository;
import com.ecommerce.trenzio.repository.ProductRepository;
import com.ecommerce.trenzio.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor  // Lombok annotation to auto-generate constructor for final fields
public class CartItemServiceImpl implements CartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    @Transactional
    public CartItemDTO addCartItem(Long cartId, CartItemDTO cartItemDTO) {
        // Retrieve the cart and product associated with the CartItem
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));

        Product product = productRepository.findById(cartItemDTO.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + cartItemDTO.getProductId()));

        // Map the DTO to a CartItem entity
        CartItem cartItem = cartItemMapper.dtoToCartItem(cartItemDTO);
        cartItem.setCart(cart);  // Associate with the cart
        cartItem.setProduct(product);  // Associate with the product
        cartItem.setSubtotal(cartItem.calculateSubTotal());  // Calculate subtotal

        // Add the CartItem to the Cart
        cart.getCartItems().add(cartItem);
        cart.calculateTotalPrice();  // Recalculate total price for the cart

        // Save the CartItem and Cart
        CartItem savedCartItem = cartItemRepository.save(cartItem);
        cartRepository.save(cart);  // Save the cart with updated items

        return cartItemMapper.cartItemToDTO(savedCartItem);
    }

    @Override
    @Transactional
    public CartItemDTO updateCartItem(Long cartId, Long cartItemId, CartItemDTO cartItemDTO) {
        // Retrieve the existing CartItem
        CartItem existingCartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with ID: " + cartItemId));

        // Update the CartItem's fields
        existingCartItem.setQuantity(cartItemDTO.getQuantity());
        existingCartItem.setDiscount(cartItemDTO.getDiscount());
        existingCartItem.setProductPrice(cartItemDTO.getProductPrice());
        existingCartItem.setSubtotal(existingCartItem.calculateSubTotal());  // Recalculate subtotal

        // Save the updated CartItem
        CartItem updatedCartItem = cartItemRepository.save(existingCartItem);

        // Update the Cart's total price after the CartItem changes
        Cart cart = existingCartItem.getCart();
        cart.calculateTotalPrice();
        cartRepository.save(cart);

        return cartItemMapper.cartItemToDTO(updatedCartItem);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long cartId, Long cartItemId) {
        // Retrieve the CartItem and remove it from the Cart
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with ID: " + cartItemId));

        Cart cart = cartItem.getCart();
        cart.getCartItems().remove(cartItem);  // Remove CartItem from the Cart
        cart.calculateTotalPrice();  // Recalculate total price
        cartRepository.save(cart);  // Save the updated Cart

        // Delete the CartItem
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartItemDTO> getAllCartItems(Long cartId) {
        // Retrieve all CartItems for the specified Cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found with ID: " + cartId));
        return cart.getCartItems().stream()
                .map(cartItemMapper::cartItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CartItemDTO getCartItemById(Long cartItemId) {
        // Retrieve the CartItem by its ID
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("CartItem not found with ID: " + cartItemId));
        return cartItemMapper.cartItemToDTO(cartItem);
    }
}
