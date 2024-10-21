package com.ecommerce.trenzio.controller;

import com.ecommerce.trenzio.dto.CartItemDTO;
import com.ecommerce.trenzio.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carts/{cartId}/items")
@RequiredArgsConstructor
public class CartItemController {

    private final CartItemService cartItemService;

    @PostMapping
    public ResponseEntity<CartItemDTO> addCartItem(@PathVariable Long cartId, @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO createdCartItem = cartItemService.addCartItem(cartId, cartItemDTO);
        return ResponseEntity.ok(createdCartItem);
    }

    @PutMapping("/{cartItemId}")
    public ResponseEntity<CartItemDTO> updateCartItem(
            @PathVariable Long cartId,
            @PathVariable Long cartItemId,
            @RequestBody CartItemDTO cartItemDTO) {
        CartItemDTO updatedCartItem = cartItemService.updateCartItem(cartId, cartItemId, cartItemDTO);
        return ResponseEntity.ok(updatedCartItem);
    }

    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Void> deleteCartItem(@PathVariable Long cartId, @PathVariable Long cartItemId) {
        cartItemService.deleteCartItem(cartId, cartItemId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<CartItemDTO>> getAllCartItems(@PathVariable Long cartId) {
        List<CartItemDTO> cartItems = cartItemService.getAllCartItems(cartId);
        return ResponseEntity.ok(cartItems);
    }

    @GetMapping("/{cartItemId}")
    public ResponseEntity<CartItemDTO> getCartItemById(@PathVariable Long cartItemId) {
        CartItemDTO cartItem = cartItemService.getCartItemById(cartItemId);
        return ResponseEntity.ok(cartItem);
    }
}
