package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.LoginRequest;
import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.User;

public interface AuthService {
    User registerUser(UserDTO userDTO);
    User findUserByUsername(String username);
}
