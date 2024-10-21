package com.ecommerce.trenzio.dto;

import com.ecommerce.trenzio.model.AppRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long userId;
    @NotBlank(message = "Username is required")
    private String username;
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank
    private String password;
    private Set<AppRole> roles;           // List of role names
    private List<AddressDTO> addresses;  // List of user addresses
    private CartDTO cart;                // User's cart (if they are a buyer)
    private Set<OrderDTO> orders;       // User's past orders (optional)
    private List<ProductDTO> products;   // User's products if they are a seller (optional)
}
