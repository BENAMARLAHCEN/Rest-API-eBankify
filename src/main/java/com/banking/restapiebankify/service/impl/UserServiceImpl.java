package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.exception.UnauthorizedAccessException;
import com.banking.restapiebankify.exception.UserNotFoundException;
import com.banking.restapiebankify.mapper.UserMapper;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.RoleService;
import com.banking.restapiebankify.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final BankAccountRepository bankAccountRepository;


    public User updateProfile(Long userId, User updateData) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!existingUser.getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        existingUser.setBirthday(updateData.getBirthday());
        existingUser.setMonthlyIncome(updateData.getMonthlyIncome());
        existingUser.setCollateralAvailable(updateData.getCollateralAvailable());

        return userRepository.save(existingUser);
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!user.getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }

        // Verify current password
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new UnauthorizedAccessException("Current password is incorrect");
        }

        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Transactional
    public void deleteAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!user.getUsername().equals(username)) {
            throw new UnauthorizedAccessException("Unauthorized access");
        }
        // Delete all associated bank accounts first
        bankAccountRepository.deleteAll(bankAccountRepository.findByUser(user));

        // Delete the user
        userRepository.delete(user);
    }

    @Override
    public User registerUser(UserDTO userDTO) {
        // Convert UserDTO to User Entity
        User user = UserMapper.INSTANCE.toUser(userDTO);

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
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public User updateUser(Long id, UserDTO userDTO) {
        User existingUser = findUserById(id);

        // Update only the allowed fields
        if (userDTO.getBirthday() != null) {
            existingUser.setBirthday(userDTO.getBirthday());
        }
        if (userDTO.getMonthlyIncome() != null) {
            existingUser.setMonthlyIncome(userDTO.getMonthlyIncome());
        }
        if (userDTO.getCollateralAvailable() != null) {
            existingUser.setCollateralAvailable(userDTO.getCollateralAvailable());
        }
        if (userDTO.getCustomerSince() != null) {
            existingUser.setCustomerSince(userDTO.getCustomerSince());
        }
        if (userDTO.getRole() != null) {
            Role role = roleService.findRoleByName(userDTO.getRole());
            existingUser.setRole(role);
        }

        return userRepository.save(existingUser);
    }

    @Override
    public User updateUserRole(Long id, String roleName) {
        User user = findUserById(id);
        Role role = roleService.findRoleByName(roleName);
        user.setRole(role);
        return userRepository.save(user);
    }
}
