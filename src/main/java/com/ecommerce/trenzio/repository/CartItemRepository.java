package com.ecommerce.trenzio.repository;

import com.ecommerce.trenzio.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Find all cart items by cart ID
    List<CartItem> findByCartCartId(Long cartId);

    // Delete all cart items by cart ID (optional)
    void deleteByCartCartId(Long cartId);

    void  deleteByCartItemId(Long cartItemId);
}
