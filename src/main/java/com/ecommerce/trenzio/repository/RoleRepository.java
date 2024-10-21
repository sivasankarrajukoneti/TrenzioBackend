package com.ecommerce.trenzio.repository;

import com.ecommerce.trenzio.model.AppRole;
import com.ecommerce.trenzio.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRoleName(AppRole roleName);
}

