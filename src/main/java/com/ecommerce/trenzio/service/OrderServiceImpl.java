package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.OrderDTO;
import com.ecommerce.trenzio.dto.OrderItemDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.OrderMapper;
import com.ecommerce.trenzio.model.Address;
import com.ecommerce.trenzio.model.Order;
import com.ecommerce.trenzio.model.OrderItem;
import com.ecommerce.trenzio.model.OrderStatus;
import com.ecommerce.trenzio.model.Product;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.repository.OrderRepository;
import com.ecommerce.trenzio.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;
    private final AddressService addressService;
    private final ProductRepository productRepository;  // To get product details

    @Override
    @Transactional
    public OrderDTO createOrder(OrderDTO orderDTO) {
        // Step 1: Retrieve User, Shipping Address, and Billing Address
        User user = userService.getUserByIdEntity(orderDTO.getUserId());
        Address shippingAddress = addressService.getAddressByIdEntity(orderDTO.getShippingAddress().getAddressId());
        Address billingAddress = addressService.getAddressByIdEntity(orderDTO.getBillingAddress().getAddressId());

        // Step 2: Map OrderDTO to Order entity and set associations
        Order order = orderMapper.dtoToOrder(orderDTO);
        order.setUser(user);
        order.setShippingAddress(shippingAddress);
        order.setBillingAddress(billingAddress);
        order.setStatus(OrderStatus.PENDING);  // Default status

        // Step 3: Handle OrderItems
        Set<OrderItem> orderItems = new HashSet<>();
        double totalAmount = 0.0;  // Calculate total amount dynamically

        for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
            Product product = productRepository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDTO.getProductId()));

            // Create and set up OrderItem
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);  // Link OrderItem to Order
            orderItem.setProduct(product);
            orderItem.setQuantity(itemDTO.getQuantity());
            orderItem.setPrice(product.getPrice());
            orderItem.setDiscount(itemDTO.getDiscount());
            orderItem.setProductPrice(product.getPrice());

            // Ensure subtotal is calculated before saving
            orderItem.setSubTotal(orderItem.calculateSubTotal());  //Explicitly set subTotal

            totalAmount += orderItem.getSubTotal();  // Add to total amount

            orderItems.add(orderItem);
        }

        // Step 4: Link OrderItems to Order
        order.setOrderItems(orderItems);
        order.setTotalPrice(totalAmount);
        order.setDiscount(orderDTO.getDiscount());

        // Step 5: Save Order and OrderItems
        Order savedOrder = orderRepository.save(order);

        // Step 6: Return saved Order as DTO
        return orderMapper.orderToDTO(savedOrder);
    }

    @Override
    public OrderDTO getOrderById(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        return orderMapper.orderToDTO(order);
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(orderMapper::orderToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(Long orderId, OrderDTO orderDTO) {
        // Retrieve existing order
        Order existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        // Update Order fields
        orderMapper.updateOrderFromDTO(orderDTO, existingOrder);

        // Handle Shipping and Billing Address updates
        if (orderDTO.getShippingAddress() != null) {
            Address shippingAddress = addressService.getAddressByIdEntity(orderDTO.getShippingAddress().getAddressId());
            existingOrder.setShippingAddress(shippingAddress);
        }
        if (orderDTO.getBillingAddress() != null) {
            Address billingAddress = addressService.getAddressByIdEntity(orderDTO.getBillingAddress().getAddressId());
            existingOrder.setBillingAddress(billingAddress);
        }

        // **Step 1: Update OrderItems in-place**
        if (orderDTO.getOrderItems() != null) {
            Set<OrderItem> updatedItems = new HashSet<>();
            for (OrderItemDTO itemDTO : orderDTO.getOrderItems()) {
                Product product = productRepository.findById(itemDTO.getProductId())
                        .orElseThrow(() -> new ResourceNotFoundException("Product not found: " + itemDTO.getProductId()));

                // Check if the item already exists in the order
                OrderItem existingItem = existingOrder.getOrderItems().stream()
                        .filter(item -> item.getId().equals(itemDTO.getOrderItemId()))
                        .findFirst()
                        .orElse(null);

                if (existingItem != null) {
                    // Update the existing item
                    existingItem.setProduct(product);
                    existingItem.setQuantity(itemDTO.getQuantity());
                    existingItem.setPrice(itemDTO.getPrice());
                    existingItem.setDiscount(itemDTO.getDiscount());
                    existingItem.setProductPrice(itemDTO.getPrice());
                    existingItem.setSubTotal(existingItem.calculateSubTotal());
                    updatedItems.add(existingItem);
                } else {
                    // Create a new OrderItem
                    OrderItem newItem = new OrderItem();
                    newItem.setOrder(existingOrder);
                    newItem.setProduct(product);
                    newItem.setQuantity(itemDTO.getQuantity());
                    newItem.setPrice(itemDTO.getPrice());
                    newItem.setDiscount(itemDTO.getDiscount());
                    newItem.setProductPrice(itemDTO.getPrice());
                    newItem.setSubTotal(newItem.calculateSubTotal());
                    updatedItems.add(newItem);
                }
            }

            // Remove OrderItems that are no longer present in the DTO
            existingOrder.getOrderItems().removeIf(item -> updatedItems.stream()
                    .noneMatch(updatedItem -> updatedItem.getId().equals(item.getId())));

            // Add the updated OrderItems
            existingOrder.getOrderItems().clear();
            existingOrder.getOrderItems().addAll(updatedItems);
        }

        // Save the updated Order
        Order updatedOrder = orderRepository.save(existingOrder);

        // Return updated Order as DTO
        return orderMapper.orderToDTO(updatedOrder);
    }

    @Override
    public OrderDTO updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));

        // Update status and related dates
        order.setStatus(newStatus);
        if (newStatus == OrderStatus.SHIPPED) {
            order.setShippingDate(LocalDateTime.now());
        } else if (newStatus == OrderStatus.DELIVERED) {
            order.setDeliveryDate(LocalDateTime.now());
        }

        Order updatedOrder = orderRepository.save(order);
        return orderMapper.orderToDTO(updatedOrder);
    }

    @Override
    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found: " + orderId));
        orderRepository.delete(order);
    }
}
