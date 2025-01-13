package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.LoginRequest;
import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.dto.UserResponse;
import com.banking.restapiebankify.model.User;

public interface AuthService {
    UserResponse registerUser(UserDTO userDTO);
    User findUserByUsername(String username);
}
