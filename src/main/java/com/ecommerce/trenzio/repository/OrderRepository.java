package com.ecommerce.trenzio.repository;

import com.ecommerce.trenzio.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    // Custom query methods can be added here if needed
}
