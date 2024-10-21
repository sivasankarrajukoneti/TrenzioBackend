package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.UserDTO;
import com.ecommerce.trenzio.exception.ResourceNotFoundException;
import com.ecommerce.trenzio.mapper.UserMapper;
import com.ecommerce.trenzio.model.User;
import com.ecommerce.trenzio.repository.UserRepository;
import com.ecommerce.trenzio.service.RoleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleService roleService;  // Add RoleService mock

    @InjectMocks
    private UserServiceImpl userService;  // Inject mocks into UserServiceImpl

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        // Initialize the mocks and inject them into the service
        MockitoAnnotations.openMocks(this);

        // Create a dummy user for testing
        user = new User();
        user.setUserId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");

        // Create a dummy UserDTO
        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
    }

    @Test
    void testFindUserById() {
        // Mock repository and mapper behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // Mock the findById call
        when(userMapper.userToDTO(user)).thenReturn(userDTO);             // Mock the userToDTO mapping

        // Call the service method
        UserDTO foundUser = userService.getUserById(1L);

        // Assertions to verify behavior
        assertNotNull(foundUser);
        assertEquals(user.getUserId(), foundUser.getUserId());
        assertEquals(user.getUsername(), foundUser.getUsername());
        assertEquals(user.getEmail(), foundUser.getEmail());

        // Verify the interaction with repository
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testCreateUser() {
        // Mock the repository save call
        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.dtoToUser(userDTO, roleService)).thenReturn(user);  // Updated to use RoleService
        when(userMapper.userToDTO(user)).thenReturn(userDTO);

        // Call the service method
        UserDTO createdUser = userService.createUser(userDTO);

        // Assertions
        assertNotNull(createdUser);
        assertEquals(userDTO.getUsername(), createdUser.getUsername());

        // Verify interactions
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).dtoToUser(any(UserDTO.class), eq(roleService));  // Verify with RoleService
    }

    @Test
    void testFindUserById_UserNotFound() {
        // Mock repository to return an empty Optional
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // Call the service method and expect an exception
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getUserById(1L);
        });

        // Verify the exception message
        String expectedMessage = "User Not Found";
        String actualMessage = exception.getMessage();
        System.out.println("Actual exception message: " + actualMessage);  // Debugging line
        assertTrue(actualMessage.contains(expectedMessage));

        // Verify that the repository method was called once
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testUpdateUser() {
        // Mock repository and mapper behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));  // Mock existing user
        when(userMapper.dtoToUser(userDTO, roleService)).thenReturn(user);  // Updated to use RoleService
        when(userMapper.userToDTO(user)).thenReturn(userDTO);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Call the service method
        UserDTO updatedUser = userService.updateUser(1L, userDTO);

        // Assertions
        assertNotNull(updatedUser);
        assertEquals(userDTO.getUsername(), updatedUser.getUsername());

        // Verify interactions
        verify(userRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testDeleteUser() {
        // Mock repository behavior
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // Call the service method
        userService.deleteUser(1L);

        // Verify that deleteById was called
        verify(userRepository, times(1)).deleteById(1L);
    }

}
