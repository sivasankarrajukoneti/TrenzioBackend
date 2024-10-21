package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.model.AppRole;
import com.ecommerce.trenzio.model.Role;
import com.ecommerce.trenzio.service.RoleService;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.Context;

import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    // Custom method to map Set<Role> to Set<String> (role names as String)
    @Named("mapRolesToNames")
    default Set<String> mapRolesToNames(Set<Role> roles) {
        return roles.stream()
                .map(role -> role.getRoleName().name())  // Map AppRole enum to its String name
                .collect(Collectors.toSet());
    }

    // Custom method to map Set<String> (role names as String) to Set<Role>
    @Named("mapNamesToRoles")
    default Set<Role> mapNamesToRoles(Set<String> roleNames, @Context RoleService roleService) {
        return roleNames.stream()
                .map(name -> roleService.findByRoleName(AppRole.valueOf(name)))  // Use RoleService to fetch Role by AppRole
                .collect(Collectors.toSet());
    }
}
