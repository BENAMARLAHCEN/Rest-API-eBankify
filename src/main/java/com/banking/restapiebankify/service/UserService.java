package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.User;

public interface UserService {
    User registerUser(UserDTO userDTO);
    User findUserByUsername(String username);

    void deleteAccount(Long userId);

    void changePassword(Long userId, String currentPassword, String newPassword);

    User updateProfile(Long userId, User updateData);
}
