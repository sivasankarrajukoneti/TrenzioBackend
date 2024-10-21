package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.PaymentDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.PaymentMapper;
import com.ecommerce.trenzio.model.Order;
import com.ecommerce.trenzio.model.Payment;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.repository.OrderRepository;
import com.ecommerce.trenzio.repository.PaymentRepository;
import com.ecommerce.trenzio.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    @Override
    @Transactional
    public PaymentDTO createPayment(PaymentDTO paymentDTO) {
        // Retrieve User and Order entities based on IDs in DTO
        User user = userRepository.findById(paymentDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + paymentDTO.getUserId()));
        Order order = orderRepository.findById(paymentDTO.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + paymentDTO.getOrderId()));

        // Map PaymentDTO to Payment entity and set associations
        Payment payment = paymentMapper.dtoToPayment(paymentDTO);
        payment.setUser(user);
        payment.setOrder(order);

        // Save and return the PaymentDTO
        Payment savedPayment = paymentRepository.save(payment);
        return paymentMapper.paymentToDTO(savedPayment);
    }

    @Override
    public PaymentDTO getPaymentById(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        return paymentMapper.paymentToDTO(payment);
    }

    @Override
    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::paymentToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public PaymentDTO updatePayment(Long paymentId, PaymentDTO paymentDTO) {
        // Retrieve existing payment
        Payment existingPayment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));

        // Map updated fields from DTO to entity
        paymentMapper.updatePaymentFromDTO(paymentDTO, existingPayment);

        // Manually update associations if provided in DTO
        if (paymentDTO.getUserId() != null) {
            User user = userRepository.findById(paymentDTO.getUserId())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found: " + paymentDTO.getUserId()));
            existingPayment.setUser(user);
        }

        if (paymentDTO.getOrderId() != null) {
            Order order = orderRepository.findById(paymentDTO.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + paymentDTO.getOrderId()));
            existingPayment.setOrder(order);
        }

        // Save and return the updated PaymentDTO
        Payment updatedPayment = paymentRepository.save(existingPayment);
        return paymentMapper.paymentToDTO(updatedPayment);
    }

    @Override
    @Transactional
    public void deletePayment(Long paymentId) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new ResourceNotFoundException("Payment not found: " + paymentId));
        paymentRepository.delete(payment);
    }
}
