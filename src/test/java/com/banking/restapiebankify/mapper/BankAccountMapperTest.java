package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.BankAccountDTO;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.AccountStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class BankAccountMapperTest {

    private final BankAccountMapper mapper = BankAccountMapper.INSTANCE;

    @Test
    void toBankAccountDTO_ShouldMapBankAccountToDTO() {
        User user = new User();
        user.setId(1L);

        BankAccount bankAccount = BankAccount.builder()
                .id(1L)
                .accountNumber("123456789")
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE)
                .user(user)
                .build();

        BankAccountDTO dto = mapper.toBankAccountDTO(bankAccount);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("123456789", dto.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), dto.getBalance());
        assertEquals("ACTIVE", dto.getStatus());
        assertEquals(1L, dto.getUserId());
    }

    @Test
    void toBankAccount_ShouldMapDTOToBankAccount() {
        BankAccountDTO dto = BankAccountDTO.builder()
                .id(1L)
                .accountNumber("123456789")
                .balance(BigDecimal.valueOf(1000))
                .status("ACTIVE")
                .userId(1L)
                .build();

        BankAccount bankAccount = mapper.toBankAccount(dto);

        assertNotNull(bankAccount);
        assertEquals(1L, bankAccount.getId());
        assertEquals("123456789", bankAccount.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), bankAccount.getBalance());
        assertEquals(AccountStatus.ACTIVE, bankAccount.getStatus());
        assertNotNull(bankAccount.getUser());
        assertEquals(1L, bankAccount.getUser().getId());
    }

    @Test
    void toBankAccountDTO_ShouldHandleNullBankAccount() {
        BankAccountDTO dto = mapper.toBankAccountDTO(null);
        assertNull(dto);
    }

    @Test
    void toBankAccount_ShouldHandleNullDTO() {
        BankAccount bankAccount = mapper.toBankAccount(null);
        assertNull(bankAccount);
    }
}