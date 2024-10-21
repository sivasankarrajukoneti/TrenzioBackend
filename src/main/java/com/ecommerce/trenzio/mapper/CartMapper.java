package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.CartDTO;
import com.ecommerce.trenzio.model.Cart;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = CartItemMapper.class)
public interface CartMapper {

    // Map Cart entity to CartDTO
    @Mapping(target = "cartItems", source = "cartItems")  // Map cart items from Cart entity to DTO
    CartDTO cartToDTO(Cart cart);

    // Map CartDTO to Cart entity
    @Mapping(target = "user", ignore = true)  // User will be set manually in service
    @Mapping(target = "cartItems", ignore = true)  // CartItems will be set manually in service
    Cart dtoToCart(CartDTO cartDTO);
}
