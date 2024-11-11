package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.AuthService;
import com.banking.restapiebankify.service.RoleService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleService roleService;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository, RoleService roleService, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        User user = UserMapper.toUser(userDTO);

        // Encode password
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        // Assign default role if not provided (e.g., USER)
        Role role = roleService.findRoleByName(userDTO.getRole() != null ? userDTO.getRole() : "USER");
        user.setRole(role);

        // Save user entity
        return userRepository.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        return userOptional.orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public String login(String username, String password) {
        User user = findUserByUsername(username);
        if (passwordEncoder.matches(password, user.getPassword())) {
            return jwtUtil.generateToken(username);
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

}
