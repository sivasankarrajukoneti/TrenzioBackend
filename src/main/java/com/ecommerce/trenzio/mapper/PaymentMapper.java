package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.PaymentDTO;
import com.ecommerce.trenzio.model.Payment;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {UserMapper.class, OrderMapper.class})
public interface PaymentMapper {

    // Map PaymentDTO to Payment entity
    @Mapping(target = "user", ignore = true)  // Manually handle User mapping in service
    @Mapping(target = "order", ignore = true)  // Manually handle Order mapping in service
    Payment dtoToPayment(PaymentDTO paymentDTO);

    // Map Payment entity to PaymentDTO
    @Mapping(target = "userId", source = "user.userId")  // Map User entity's ID to DTO
    @Mapping(target = "orderId", source = "order.id")    // Map Order entity's ID to DTO
    PaymentDTO paymentToDTO(Payment payment);

    // Update Payment entity from PaymentDTO
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)  // Ignore user during update
    @Mapping(target = "order", ignore = true) // Ignore order during update
    void updatePaymentFromDTO(PaymentDTO paymentDTO, @MappingTarget Payment payment);
}
