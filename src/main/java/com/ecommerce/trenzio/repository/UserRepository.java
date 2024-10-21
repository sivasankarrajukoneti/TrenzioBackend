package com.ecommerce.trenzio.repository;

import com.ecommerce.trenzio.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // Add any custom query methods if needed
    User findByUsername(String username);
}
