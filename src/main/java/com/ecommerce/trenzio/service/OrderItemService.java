package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.OrderItemDTO;
import java.util.List;

public interface OrderItemService {
    List<OrderItemDTO> getAllOrderItems();

    OrderItemDTO getOrderItemById(Long id);

    OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO);

    OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO);

    void deleteOrderItem(Long id);
}
