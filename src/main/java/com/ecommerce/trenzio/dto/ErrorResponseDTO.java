package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {
    private String message;
    private String details;    // Detailed error information (optional)
    private int statusCode;    // HTTP status code
    private LocalDateTime timestamp; // Time the error occurred
}
