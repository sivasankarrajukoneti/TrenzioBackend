package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.UserDTO;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.model.Role;
import com.ecommerce.trenzio.model.AppRole;
import com.ecommerce.trenzio.service.RoleService;
import org.mapstruct.*;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, AddressMapper.class, CartMapper.class, OrderMapper.class, ProductMapper.class})
public interface UserMapper {

    // Map User entity to UserDTO
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapRolesToAppRoles")
    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "cart", source = "cart")
    @Mapping(target = "orders", source = "orders")
    @Mapping(target = "products", source = "products")
    UserDTO userToDTO(User user);

    // Map UserDTO to User entity, with RoleService as context
    @Mapping(target = "roles", source = "roles", qualifiedByName = "mapAppRolesToRoles")
    @Mapping(target = "addresses", source = "addresses")
    @Mapping(target = "cart", source = "cart")
    @Mapping(target = "orders", source = "orders")
    @Mapping(target = "products", source = "products")
    User dtoToUser(UserDTO userDTO, @Context RoleService roleService);

    // Map Role entities to AppRole (enum)
    @Named("mapRolesToAppRoles")
    default Set<AppRole> mapRolesToAppRoles(Set<Role> roles) {
        return roles.stream()
                .map(Role::getRoleName)  // Assuming Role entity has `getRoleName()` returning AppRole enum
                .collect(Collectors.toSet());
    }

    // Map AppRole (enum) to Role entities using RoleService
    @Named("mapAppRolesToRoles")
    default Set<Role> mapAppRolesToRoles(Set<AppRole> appRoles, @Context RoleService roleService) {
        return appRoles.stream()
                .map(roleService::findByRoleName)  // Use RoleService to fetch Role by AppRole
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }

    // Update User entity from UserDTO, without touching unmentioned fields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "roles", ignore = true)  // Ignore roles, handled manually
    @Mapping(target = "userId", ignore = true)  // Ignore userId as it's not updatable
    void updateUserFromDTO(UserDTO userDTO, @MappingTarget User user, @Context RoleService roleService);

    // Manually update roles after mapping
    default void updateRoles(UserDTO userDTO, User user, @Context RoleService roleService) {
        if (userDTO.getRoles() != null && !userDTO.getRoles().isEmpty()) {
            Set<Role> updatedRoles = userDTO.getRoles().stream()
                    .map(roleService::findByRoleName)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toSet());
            user.setRoles(updatedRoles);  // Set the mapped roles to the user
        }
    }
}
