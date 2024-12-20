package com.banking.restapiebankify.config;

import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtTokenProviderTest {

    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        jwtTokenProvider = new JwtTokenProvider();
        jwtTokenProvider.setJwtSecret("01234567890123456789012345678901"); // 32 bytes key for HMAC
        jwtTokenProvider.setJwtExpiration(3600000); // 1 hour in milliseconds
        jwtTokenProvider.setClockSkew(1000); // 1 second clock skew
    }

    @Test
    void generateToken_ShouldReturnValidToken() {
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        String token = jwtTokenProvider.generateToken(user);

        assertNotNull(token);

        String username = jwtTokenProvider.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void validateToken_ShouldReturnTrueForValidToken() {
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        when(userDetails.getUsername()).thenReturn("testuser");

        String token = jwtTokenProvider.generateToken(user);

        assertTrue(jwtTokenProvider.validateToken(token, userDetails));
    }

    @Test
    void validateToken_ShouldReturnFalseForExpiredToken() {
        jwtTokenProvider.setJwtExpiration(-1000); // Expired 1 second ago

        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        String token = jwtTokenProvider.generateToken(user);

        assertFalse(jwtTokenProvider.validateToken(token, userDetails));
    }

    @Test
    void extractUsername_ShouldReturnUsernameFromToken() {
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        String token = jwtTokenProvider.generateToken(user);

        String username = jwtTokenProvider.extractUsername(token);
        assertEquals("testuser", username);
    }

    @Test
    void extractExpiration_ShouldReturnExpirationDate() {
        Role role = new Role();
        role.setName("USER");

        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setRole(role);

        String token = jwtTokenProvider.generateToken(user);

        Date expiration = jwtTokenProvider.extractExpiration(token);
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }
}