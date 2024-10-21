package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.ShippingDTO;
import com.ecommerce.trenzio.model.Shipping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ShippingMapper {

    // Map Shipping entity to ShippingDTO
    @Mapping(target = "orderId", source = "order.id") // Extract order ID from the Order entity
    ShippingDTO shippingToDTO(Shipping shipping);

    // Map ShippingDTO to Shipping entity
    @Mapping(target = "order.id", source = "orderId") // Set the order reference by order ID
    Shipping dtoToShipping(ShippingDTO shippingDTO);
}
