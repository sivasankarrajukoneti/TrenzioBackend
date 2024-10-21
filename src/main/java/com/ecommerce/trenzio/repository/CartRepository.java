package com.ecommerce.trenzio.repository;

import com.ecommerce.trenzio.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    // Find cart by user ID (assuming each user has one cart)
    Optional<Cart> findByUserUserId(Long userId);

    // Delete cart by user ID (optional, if you want to add this functionality)
    void deleteByUserUserId(Long userId);
}
