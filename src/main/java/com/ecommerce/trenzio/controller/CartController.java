package com.ecommerce.trenzio.controller;

import com.ecommerce.trenzio.dto.CartDTO;
import com.ecommerce.trenzio.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/{userId}")
    public ResponseEntity<CartDTO> createCart(@PathVariable Long userId) {
        CartDTO createdCart = cartService.createCart(userId);
        return ResponseEntity.ok(createdCart);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDTO> getCartById(@PathVariable Long cartId) {
        CartDTO cart = cartService.getCartById(cartId);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDTO> updateCart(@PathVariable Long cartId, @RequestBody CartDTO cartDTO) {
        CartDTO updatedCart = cartService.updateCart(cartId, cartDTO);
        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/{cartId}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{cartId}/clear")
    public ResponseEntity<CartDTO> clearCart(@PathVariable Long cartId) {
        CartDTO clearedCart = cartService.clearCart(cartId);
        return ResponseEntity.ok(clearedCart);
    }
}
