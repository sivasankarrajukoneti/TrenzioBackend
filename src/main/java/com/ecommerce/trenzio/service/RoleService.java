package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.model.AppRole;
import com.ecommerce.trenzio.model.Role;
import com.ecommerce.trenzio.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role findByRoleName(AppRole roleName) {
        return roleRepository.findByRoleName(roleName);
    }
}
