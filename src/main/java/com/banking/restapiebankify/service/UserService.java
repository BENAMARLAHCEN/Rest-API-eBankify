package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User findUserByUsername(String username);

    void deleteAccount(Long userId);

    void changePassword(Long userId, String currentPassword, String newPassword);

    User updateProfile(Long userId, User updateData);

    User findUserById(Long id);
    Page<User> getAllUsers(Pageable pageable);
    User updateUser(Long id, UserDTO userDTO);
    User updateUserRole(Long id, String role);
}
