package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.PaymentDTO;
import java.util.List;

public interface PaymentService {
    PaymentDTO createPayment(PaymentDTO paymentDTO);
    PaymentDTO getPaymentById(Long paymentId);
    List<PaymentDTO> getAllPayments();
    PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO);
    void deletePayment(Long paymentId);
}
