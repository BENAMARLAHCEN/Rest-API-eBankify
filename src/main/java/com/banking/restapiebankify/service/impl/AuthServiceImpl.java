package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.dto.UserResponse;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.repository.RoleRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.AuthService;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadCredentialsException("Invalid user credentials"));
    }

    @Override
    public UserResponse registerUser(UserDTO userDTO) {
        User user = UserMapper.INSTANCE.toUser(userDTO);

        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));  // Use injected encoder

        Role role = roleRepository.findByName(
                        Optional.ofNullable(userDTO.getRole()).orElse("USER"))
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRole(role);

        return UserMapper.INSTANCE.toUserResponse(userRepository.save(user));
    }

}
