package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDTO {
    private String username;
    private String token;  // JWT token or any other authentication token
}
