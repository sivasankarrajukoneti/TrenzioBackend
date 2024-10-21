package com.ecommerce.trenzio.dto;

import com.ecommerce.trenzio.model.PaymentMethod;
import com.ecommerce.trenzio.model.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

    private Long paymentId;
    private Double amount;
    private PaymentMethod paymentMethod;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentDate;
    private Long userId;  // Reference to the User who made the payment
    private Long orderId;  // Reference to the Order associated with the payment
}
