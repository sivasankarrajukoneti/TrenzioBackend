package com.ecommerce.trenzio.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationErrorDTO {
    private String field;       // The name of the field with the validation error
    private String errorMessage; // The validation error message
}
