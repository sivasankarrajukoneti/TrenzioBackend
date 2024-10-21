package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.OrderDTO;
import com.ecommerce.trenzio.model.OrderStatus;

import java.util.List;

public interface OrderService {
    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO getOrderById(Long orderId);

    List<OrderDTO> getAllOrders();

    OrderDTO updateOrder(Long orderId, OrderDTO orderDTO);

    void deleteOrder(Long orderId);

    OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus);
}
