package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.RoleService;
import com.banking.restapiebankify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        // Convert UserDTO to User Entity
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
}
