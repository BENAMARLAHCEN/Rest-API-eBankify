package com.banking.restapiebankify.impl;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.repository.RoleRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUser_ShouldReturnUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole("USER");

        Role mockRole = new Role();
        mockRole.setName("USER");

        User mockUser = new User();
        mockUser.setUsername("testuser");
        mockUser.setEmail("test@example.com");
        mockUser.setRole(mockRole);

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(mockRole));
        when(passwordEncoder.encode("password123")).thenReturn("$2a$10$w2WBwPB82qJmqRbXTxOrw.3MqEFSRBLKjDMqkq6TEM.k8/32/X.Vi");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.setId(1L);
            return savedUser;
        });

        User registeredUser = authService.registerUser(userDTO);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("test@example.com", registeredUser.getEmail());
        assertEquals("USER", registeredUser.getRole().getName());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenUsernameExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> authService.registerUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenEmailExists() {
        UserDTO userDTO = new UserDTO();
        userDTO.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(new User()));

        assertThrows(RuntimeException.class, () -> authService.registerUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void registerUser_ShouldThrowException_WhenRoleNotFound() {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername("testuser");
        userDTO.setEmail("test@example.com");
        userDTO.setPassword("password123");
        userDTO.setRole("INVALID_ROLE");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        when(roleRepository.findByName("INVALID_ROLE")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.registerUser(userDTO));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void findUserByUsername_ShouldReturnUser() {
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        User user = authService.findUserByUsername("testuser");

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void findUserByUsername_ShouldThrowException_WhenUserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        assertThrows(BadCredentialsException.class, () -> authService.findUserByUsername("testuser"));
    }
}
