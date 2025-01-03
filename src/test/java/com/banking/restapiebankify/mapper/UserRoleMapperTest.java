package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.RoleDTO;
import com.banking.restapiebankify.dto.UserDTO;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleMapperTest {

    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final RoleMapper roleMapper = RoleMapper.INSTANCE;

    @Test
    void toUserDTO_ShouldMapUserToDTO() {
        Role role = new Role();
        role.setId(1L);
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

        UserDTO dto = userMapper.toUserDTO(user);

        assertNotNull(dto);
        assertEquals("testuser", dto.getUsername());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), dto.getBirthday());
        assertEquals(BigDecimal.valueOf(5000), dto.getMonthlyIncome());
        assertEquals("Property", dto.getCollateralAvailable());
        assertEquals("USER", dto.getRole());
    }

    @Test
    void toUser_ShouldMapDTOToUser() {
        UserDTO dto = UserDTO.builder()
                .username("testuser")
                .password("password123")
                .email("test@example.com")
                .birthday(LocalDate.of(1990, 1, 1))
                .monthlyIncome(BigDecimal.valueOf(5000))
                .collateralAvailable("Property")
                .customerSince(LocalDate.of(2010, 1, 1))
                .role("USER")
                .build();

        User user = userMapper.toUser(dto);

        assertNotNull(user);
        assertEquals("testuser", user.getUsername());
        assertEquals("test@example.com", user.getEmail());
        assertEquals(LocalDate.of(1990, 1, 1), user.getBirthday());
        assertEquals(BigDecimal.valueOf(5000), user.getMonthlyIncome());
        assertEquals("Property", user.getCollateralAvailable());
        assertNull(user.getRole(), "Role should be null because it is ignored during mapping");
    }

    @Test
    void toRoleDTO_ShouldMapRoleToDTO() {
        Role role = new Role();
        role.setId(1L);
        role.setName("ADMIN");

        RoleDTO dto = roleMapper.toRoleDTO(role);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("ADMIN", dto.getName());
    }

    @Test
    void toRole_ShouldMapDTOToRole() {
        RoleDTO dto = RoleDTO.builder()
                .id(1L)
                .name("ADMIN")
                .build();

        Role role = roleMapper.toRole(dto);

        assertNotNull(role);
        assertEquals(1L, role.getId());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void toUserDTO_ShouldHandleNullUser() {
        UserDTO dto = userMapper.toUserDTO(null);
        assertNull(dto);
    }

    @Test
    void toUser_ShouldHandleNullDTO() {
        User user = userMapper.toUser(null);
        assertNull(user);
    }

    @Test
    void toRoleDTO_ShouldHandleNullRole() {
        RoleDTO dto = roleMapper.toRoleDTO(null);
        assertNull(dto);
    }

    @Test
    void toRole_ShouldHandleNullRoleDTO() {
        Role role = roleMapper.toRole(null);
        assertNull(role);
    }
}