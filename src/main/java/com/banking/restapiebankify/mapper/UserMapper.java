package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.Role;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    // Convert UserDTO to User entity
    public static User toUser(UserDTO userDTO) {
        if (userDTO == null) {
            return null;
        }

        User user = new User();
        user.setId(userDTO.getId());
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setBirthday(userDTO.getBirthday());
        user.setMonthlyIncome(userDTO.getMonthlyIncome());
        user.setCollateralAvailable(userDTO.getCollateralAvailable());
        user.setCustomerSince(userDTO.getCustomerSince());
        return user;
    }

    // Convert User entity to UserDTO
    public static UserDTO toDTO(User user) {
        if (user == null) {
            return null;
        }

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setEmail(user.getEmail());
        userDTO.setBirthday(user.getBirthday());
        userDTO.setMonthlyIncome(user.getMonthlyIncome());
        userDTO.setCollateralAvailable(user.getCollateralAvailable());
        userDTO.setCustomerSince(user.getCustomerSince());
        userDTO.setRole(user.getRole() != null ? user.getRole().getName() : null);  // Use role name
        return userDTO;
    }
}