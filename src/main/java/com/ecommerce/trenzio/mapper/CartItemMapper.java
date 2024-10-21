package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.CartItemDTO;
import com.ecommerce.trenzio.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    // Map CartItem entity to CartItemDTO
    @Mapping(target = "productId", source = "product.productId")  // Map productId from Product
    @Mapping(target = "productName", source = "product.productName")  // Map productName from Product
    CartItemDTO cartItemToDTO(CartItem cartItem);

    // Map CartItemDTO to CartItem entity
    @Mapping(target = "cart", ignore = true)  // Cart will be set manually in the service
    @Mapping(target = "product", ignore = true)  // Product will be set manually in the service
    CartItem dtoToCartItem(CartItemDTO cartItemDTO);
}
