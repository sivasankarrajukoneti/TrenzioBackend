package com.ecommerce.trenzio.service;

import com.ecommerce.trenzio.dto.UserDTO;
import com.ecommerce.trenzio.model.User;

import java.util.List;

public interface UserService {
    List<UserDTO> getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO createUser(UserDTO userDTO);
    UserDTO updateUser(Long id, UserDTO userDTO);
    void deleteUser(Long id);
    User getUserByIdEntity(Long userId);
}
