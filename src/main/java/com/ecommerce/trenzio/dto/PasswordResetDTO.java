package com.ecommerce.trenzio.dto;

import lombok.Data;

@Data
public class PasswordResetDTO {
    private String email;
    private String newPassword;
    private String confirmPassword;
}
