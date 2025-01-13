package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.config.JwtTokenProvider;
import com.banking.restapiebankify.dto.AuthResponse;
import com.banking.restapiebankify.dto.LoginRequest;
import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.dto.UserResponse;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthController(AuthService authService, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(@RequestBody UserDTO UserDTO) {
        UserResponse user = authService.registerUser(UserDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = authService.findUserByUsername(loginRequest.getUsername());
        String jwt = jwtTokenProvider.generateToken(user);
        String refreshToken = jwtTokenProvider.generateRefreshToken(user);

        UserResponse userResponse = UserMapper.INSTANCE.toUserResponse(user);
        AuthResponse authResponse = new AuthResponse(jwt, refreshToken, userResponse);
        return ResponseEntity.ok(authResponse);

    }

    @GetMapping("/verify")
    public ResponseEntity<UserResponse> verifyToken(@RequestHeader("Authorization") String token) {
            token = token.substring(7);
        if (jwtTokenProvider.validateToken(token)) {
            String username = jwtTokenProvider.extractUsername(token);
            User user = authService.findUserByUsername(username);
            UserResponse userResponse = UserMapper.INSTANCE.toUserResponse(user);
            return ResponseEntity.ok(userResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (jwtTokenProvider.validateToken(refreshToken)) {
            String username = jwtTokenProvider.extractUsername(refreshToken);
            User user = authService.findUserByUsername(username);
            String jwt = jwtTokenProvider.generateToken(user);
            String newRefreshToken = jwtTokenProvider.generateRefreshToken(user);
            UserResponse userResponse = UserMapper.INSTANCE.toUserResponse(user);
            AuthResponse authResponse = new AuthResponse(jwt, newRefreshToken, userResponse);
            return ResponseEntity.ok(authResponse);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> me() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = authService.findUserByUsername(username);
        UserResponse userResponse = UserMapper.INSTANCE.toUserResponse(user);
        return ResponseEntity.ok(userResponse);
    }

}
