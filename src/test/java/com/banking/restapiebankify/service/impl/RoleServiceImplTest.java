package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private RoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findRoleByName_ShouldReturnRole() {
        Role mockRole = new Role();
        mockRole.setId(1L);
        mockRole.setName("USER");

        when(roleRepository.findByName("USER")).thenReturn(Optional.of(mockRole));

        Role role = roleService.findRoleByName("USER");

        assertNotNull(role);
        assertEquals("USER", role.getName());
        verify(roleRepository, times(1)).findByName("USER");
    }

    @Test
    void findRoleByName_ShouldThrowException_WhenRoleNotFound() {
        when(roleRepository.findByName("ADMIN")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> roleService.findRoleByName("ADMIN"));
        assertEquals("Role not found", exception.getMessage());
        verify(roleRepository, times(1)).findByName("ADMIN");
    }
}