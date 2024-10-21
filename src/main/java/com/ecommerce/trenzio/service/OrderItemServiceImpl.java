package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.OrderItemDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.OrderItemMapper;
import com.ecommerce.trenzio.model.OrderItem;
import com.ecommerce.trenzio.repository.OrderItemRepository;
import com.ecommerce.trenzio.service.OrderItemService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository, OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    @Override
    public List<OrderItemDTO> getAllOrderItems() {
        List<OrderItem> orderItems = orderItemRepository.findAll();
        if (orderItems.isEmpty()) {
            throw new ResourceNotFoundException("No Order Items Found");
        }
        return orderItems.stream()
                .map(orderItemMapper::orderItemToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public OrderItemDTO getOrderItemById(Long id) {
        OrderItem orderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order Item not found with ID: " + id));
        return orderItemMapper.orderItemToDTO(orderItem);
    }

    @Override
    @Transactional
    public OrderItemDTO createOrderItem(OrderItemDTO orderItemDTO) {
        OrderItem orderItem = orderItemMapper.dtoToOrderItem(orderItemDTO);
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.orderItemToDTO(savedOrderItem);
    }

    @Override
    @Transactional
    public OrderItemDTO updateOrderItem(Long id, OrderItemDTO orderItemDTO) {
        OrderItem existingOrderItem = orderItemRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order Item not found with ID: " + id));

        orderItemMapper.updateOrderItemFromDTO(orderItemDTO, existingOrderItem);
        OrderItem updatedOrderItem = orderItemRepository.save(existingOrderItem);
        return orderItemMapper.orderItemToDTO(updatedOrderItem);
    }

    @Override
    public void deleteOrderItem(Long id) {
        orderItemRepository.deleteById(id);
    }
}
