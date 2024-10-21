package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.AddressDTO;
import com.ecommerce.trenzio.dto.OrderDTO;
import com.ecommerce.trenzio.dto.ProductDTO;
import com.ecommerce.trenzio.dto.UserDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.*;
import com.ecommerce.trenzio.model.*;
import com.ecommerce.trenzio.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final RoleService roleService;



    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private AddressMapper addressMapper;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper, RoleService roleService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.roleService = roleService;
    }

    @Override
    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()){
            throw new ResourceNotFoundException("Users Not Found");
        }
        return users.stream().map(userMapper::userToDTO).collect(Collectors.toList());
    }

    @Override
    public UserDTO getUserById(Long id) {
        log.debug("Fetching user with ID: {}", id);
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User Not Found"));
        return userMapper.userToDTO(user);
    }

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        log.info("Creating a new user with username: {}", userDTO.getUsername());
        User user = userMapper.dtoToUser(userDTO, roleService);
        User savedUser = userRepository.save(user);
        log.debug("User saved with ID: {}", savedUser.getUserId());
        return userMapper.userToDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(Long userId, UserDTO userDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Incremental updates for each field
        if (userDTO.getUsername() != null) {
            user.setUsername(userDTO.getUsername());
        }

        if (userDTO.getEmail() != null) {
            user.setEmail(userDTO.getEmail());
        }

        if (userDTO.getPassword() != null) {
            user.setPassword(userDTO.getPassword());
        }

        // Update roles incrementally
        if (userDTO.getRoles() != null) {
            userMapper.updateRoles(userDTO, user, roleService);
        }

        // Handle incremental updates for orders
        if (userDTO.getOrders() != null) {
            updateOrders(user, userDTO.getOrders());
        }

        // Handle incremental updates for addresses
        if (userDTO.getAddresses() != null) {
            updateAddresses(user, userDTO.getAddresses());
        }

        // Handle incremental updates for products
        if (userDTO.getProducts() != null) {
            updateProducts(user, userDTO.getProducts());
        }

        // Save the updated user
        userRepository.save(user);

        // Return the updated UserDTO
        return userMapper.userToDTO(user);
    }

    // Helper method to update orders using Set<OrderDTO>
    private void updateOrders(User user, Set<OrderDTO> orderDTOs) {
        // Retrieve the existing set of orders associated with the user
        Set<Order> existingOrders = user.getOrders();

        // Convert the incoming set of OrderDTOs to a set of Order entities using the orderMapper
        Set<Order> newOrders = orderDTOs.stream()
                .map(orderMapper::dtoToOrder) // Maps each OrderDTO to an Order entity
                .collect(Collectors.toSet()); // Collects the result into a set of Order entities

        // Remove orders from the existingOrders set that are not present in the newOrders set
        // This ensures that any orders which are no longer referenced are removed
        existingOrders.removeIf(order -> !newOrders.contains(order));

        // For each newOrder in the newOrders set
        newOrders.forEach(order -> {
            // Check if the new order is already in the existing orders
            if (!existingOrders.contains(order)) {
                // If the order is not in the existing set, add it as a new order
                existingOrders.add(order);
            } else {
                // If the order already exists, update its order items (nested relationship)
                // The items of the order will be updated to match the new items in the newOrder
                updateOrderItems(order, order.getOrderItems());
            }
        });
    }


    // Helper method to update the items of an existing order incrementally
    private void updateOrderItems(Order existingOrder, Set<OrderItem> newOrderItems) {
        // Retrieve the existing set of order items associated with the order
        Set<OrderItem> existingOrderItems = existingOrder.getOrderItems();

        // Remove items from the existing set that are not present in the new set
        // This removes orphaned items, ensuring that only items present in newOrderItems remain
        existingOrderItems.removeIf(item -> !newOrderItems.contains(item));

        // For each new item in the newOrderItems set
        newOrderItems.forEach(item -> {
            // Check if the new item is already in the existing set
            if (!existingOrderItems.contains(item)) {
                // If the item is not present in the existing set, add it as a new order item
                existingOrderItems.add(item);
            } else {
                // If the item already exists, find the corresponding existing item in the set
                OrderItem existingItem = existingOrderItems.stream()
                        // Filter the stream to find the existing item that matches the new item
                        .filter(existing -> existing.equals(item))
                        .findFirst() // Find the first matching item
                        .orElse(null); // If not found, return null

                // If an existing item is found, update its details (quantity, price, discount)
                if (existingItem != null) {
                    existingItem.setQuantity(item.getQuantity()); // Update quantity
                    existingItem.setProductPrice(item.getProductPrice()); // Update product price
                    existingItem.setDiscount(item.getDiscount()); // Update discount
                }
            }
        });
    }

    // Helper method to update addresses (incremental update)
    private void updateAddresses(User user, List<AddressDTO> addressDTOs) {
        List<Address> existingAddresses = user.getAddresses();
        List<Address> newAddresses = addressDTOs.stream()
                .map(addressMapper::dtoToAddress)
                .collect(Collectors.toList());

        // Remove addresses that are no longer present
        existingAddresses.removeIf(address -> !newAddresses.contains(address));

        // Add or update the remaining addresses
        newAddresses.forEach(address -> {
            if (!existingAddresses.contains(address)) {
                existingAddresses.add(address);  // Add new addresses
            }
        });
    }

    // Helper method to update products incrementally
    private void updateProducts(User user, List<ProductDTO> productDTOs) {
        Set<Product> existingProducts = user.getProducts();
        Set<Product> newProducts = productDTOs.stream()
                .map(productMapper::dtoToProduct)
                .collect(Collectors.toSet());

        // Remove products that are no longer present
        existingProducts.removeIf(product -> !newProducts.contains(product));

        // Add or update the remaining products
        newProducts.forEach(product -> {
            if (!existingProducts.contains(product)) {
                existingProducts.add(product);  // Add new products
            }
        });
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User getUserByIdEntity(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));
    }

}
