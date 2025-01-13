package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.PasswordChangeRequest;
import com.banking.restapiebankify.dto.UserResponse;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @PutMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponse> updateProfile(
            @PathVariable Long userId,
            @RequestBody User updateData) {
        User updatedUser = userService.updateProfile(userId, updateData);
        return ResponseEntity.ok(UserMapper.INSTANCE.toUserResponse(updatedUser));
    }

    @PostMapping("/{userId}/change-password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> changePassword(
            @PathVariable Long userId,
            @RequestBody PasswordChangeRequest request) {
        userService.changePassword(userId, request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long userId) {
        userService.deleteAccount(userId);
        return ResponseEntity.ok().build();
    }
}