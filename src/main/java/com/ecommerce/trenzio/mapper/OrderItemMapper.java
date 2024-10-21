package com.ecommerce.trenzio.mapper;

import com.ecommerce.trenzio.dto.OrderItemDTO;
import com.ecommerce.trenzio.model.OrderItem;
import com.ecommerce.trenzio.model.Product;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public abstract class OrderItemMapper {

    @Autowired
    private ProductMapper productMapper;

    @Mapping(target = "orderItemId", source = "id")
    @Mapping(target = "productId", source = "product.productId")  // Map productId to the Product entity reference
    @Mapping(target = "productName", source = "product.productName")  // Map productName for display purposes
    @Mapping(target = "subTotal", ignore = true)  // Ignore subTotal during mapping, handle it manually
    public abstract OrderItemDTO orderItemToDTO(OrderItem orderItem);

    @Mapping(target = "order", ignore = true)  // Order is set manually in the service layer
    @Mapping(target = "product", ignore = true)  // Product will be set manually in the service layer
    public abstract OrderItem dtoToOrderItem(OrderItemDTO orderItemDTO);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "product", ignore = true)
    public abstract void updateOrderItemFromDTO(OrderItemDTO orderItemDTO, @MappingTarget OrderItem orderItem);

    // After mapping: Set calculated subTotal value to the DTO
    @AfterMapping
    protected void setSubTotal(OrderItem orderItem, @MappingTarget OrderItemDTO orderItemDTO) {
        if (orderItem != null) {
            orderItemDTO.setSubTotal(orderItem.getSubTotal());
        }
    }

    // After mapping: Handle associations for creating OrderItem entity from DTO
    @AfterMapping
    protected void setProduct(OrderItemDTO orderItemDTO, @MappingTarget OrderItem orderItem) {
        if (orderItemDTO.getProductId() != null) {
            Product product = new Product();
            product.setProductId(orderItemDTO.getProductId());
            orderItem.setProduct(product);
        }
    }
}
