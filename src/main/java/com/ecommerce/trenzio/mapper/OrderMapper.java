package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.OrderDTO;
import com.ecommerce.trenzio.model.Order;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {AddressMapper.class, UserMapper.class, ProductMapper.class, OrderItemMapper.class})
public interface OrderMapper {

    // Map OrderDTO to Order entity
    @Mapping(target = "user", ignore = true)  // User will be set manually in service
    @Mapping(target = "orderItems", ignore = true)  // OrderItems will be set manually in service
    @Mapping(target = "shippingAddress", ignore = true)  // Shipping address manually set in service
    @Mapping(target = "billingAddress", ignore = true)   // Billing address manually set in service
    Order dtoToOrder(OrderDTO orderDTO);

    // Map Order entity to OrderDTO
    @Mapping(target = "orderId", source = "id")  // Map entity id to DTO field
    @Mapping(target = "status", source = "status")  // Map OrderStatus to String
    @Mapping(target = "shippingAddress", source = "shippingAddress")  // Include address mapping
    @Mapping(target = "billingAddress", source = "billingAddress")  // Include address mapping
    @Mapping(target = "orderItems", source = "orderItems")  // Include order items mapping
    OrderDTO orderToDTO(Order order);

    // Update Order entity from OrderDTO without touching unmentioned fields
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "user", ignore = true)  // Ignore user during update
    @Mapping(target = "orderItems", ignore = true)  // Ignore orderItems during update
    @Mapping(target = "shippingAddress", ignore = true)  // Ignore address updates here
    @Mapping(target = "billingAddress", ignore = true)  // Ignore address updates here
    void updateOrderFromDTO(OrderDTO orderDTO, @MappingTarget Order order);
}
