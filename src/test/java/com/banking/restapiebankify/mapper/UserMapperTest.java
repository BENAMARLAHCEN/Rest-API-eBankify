package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Test
    void toUserDTO_ShouldMapUserToUserDTO() {
        Role role = new Role();
        role.setName("USER");

        User user = User.builder()
                .id(1L)
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .monthlyIncome(BigDecimal.valueOf(5000))
                .collateralAvailable("Property")
                .customerSince(LocalDate.of(2010, 1, 1))
                .role(role)
                .build();

        UserDTO userDTO = userMapper.toUserDTO(user);

        assertNotNull(userDTO);
        assertEquals("testuser", userDTO.getUsername());
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), userDTO.getBirthday());
        assertEquals(BigDecimal.valueOf(5000), userDTO.getMonthlyIncome());
        assertEquals("Property", userDTO.getCollateralAvailable());
        assertEquals("USER", userDTO.getRole());
    }

    @Test
    void toUser_ShouldMapUserDTOToUser() {
        UserDTO userDTO = UserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .monthlyIncome(BigDecimal.valueOf(5000))
                .collateralAvailable("Property")
                .customerSince(LocalDate.of(2010, 1, 1))
                .role("USER")
                .build();

        User user = userMapper.toUser(userDTO);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
        assertEquals(BigDecimal.valueOf(5000), user.getMonthlyIncome());
        assertEquals("Property", user.getCollateralAvailable());
        assertNull(user.getRole(), "Role should be null because it is ignored during mapping");
    }

    @Test
    void toUserDTO_ShouldHandleNullUser() {
        UserDTO userDTO = userMapper.toUserDTO(null);
        assertNull(userDTO);
    }

    @Test
    void toUser_ShouldHandleNullUserDTO() {
        User user = userMapper.toUser(null);
        assertNull(user);
    }
}