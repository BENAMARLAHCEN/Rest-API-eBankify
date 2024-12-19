package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.exception.BankAccountNotFoundException;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Role;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.AccountStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBankAccount_ShouldReturnCreatedAccount() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        BankAccount bankAccount = new BankAccount();
        bankAccount.setBalance(BigDecimal.valueOf(1000));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(bankAccountRepository.save(any(BankAccount.class))).thenAnswer(invocation -> {
            BankAccount savedAccount = invocation.getArgument(0);
            savedAccount.setId(1L);
            savedAccount.setAccountNumber(UUID.randomUUID().toString());
            return savedAccount;
        });

        BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount, "testUser");

        assertNotNull(createdAccount);
        assertEquals(BigDecimal.valueOf(1000), createdAccount.getBalance());
        assertEquals(1L, createdAccount.getId());
        assertNotNull(createdAccount.getAccountNumber());
        verify(userRepository, times(1)).findByUsername("testUser");
        verify(bankAccountRepository, times(1)).save(bankAccount);
    }

    @Test
    void updateBankAccount_ShouldUpdateAndReturnAccount() {
        User mockUser = new User();
        mockUser.setId(1L);

        BankAccount existingAccount = new BankAccount();
        existingAccount.setId(1L);
        existingAccount.setBalance(BigDecimal.valueOf(1000));
        existingAccount.setUser(mockUser);

        BankAccount updatedAccountDetails = new BankAccount();
        updatedAccountDetails.setBalance(BigDecimal.valueOf(2000));

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(bankAccountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(existingAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(existingAccount);

        BankAccount updatedAccount = bankAccountService.updateBankAccount(1L, updatedAccountDetails, "testUser");

        assertNotNull(updatedAccount);
        assertEquals(BigDecimal.valueOf(2000), updatedAccount.getBalance());
        verify(bankAccountRepository, times(1)).save(existingAccount);
    }

    @Test
    void deleteBankAccount_ShouldCallDeleteRepository() {
        User mockUser = new User();
        mockUser.setId(1L);

        BankAccount existingAccount = new BankAccount();
        existingAccount.setId(1L);
        existingAccount.setUser(mockUser);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(mockUser));
        when(bankAccountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(existingAccount));

        bankAccountService.deleteBankAccount(1L, "testUser");

        verify(bankAccountRepository, times(1)).delete(existingAccount);
    }

    @Test
    void getBankAccountForUserOrAdmin_ShouldReturnAccountForAdmin() {
        User adminUser = new User();
        adminUser.setId(1L);
        adminUser.setUsername("adminUser");
        adminUser.setRole(new Role().builder().name("ADMIN").build());

        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setBalance(BigDecimal.valueOf(1500));

        when(userRepository.findByUsername("adminUser")).thenReturn(Optional.of(adminUser));
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(account));

        BankAccount fetchedAccount = bankAccountService.getBankAccountForUserOrAdmin(1L, "adminUser");

        assertNotNull(fetchedAccount);
        assertEquals(BigDecimal.valueOf(1500), fetchedAccount.getBalance());
        verify(bankAccountRepository, times(1)).findById(1L);
    }

    @Test
    void blockOrActivateAccount_ShouldChangeStatus() {
        User mockUser = new User();
        mockUser.setId(1L);

        BankAccount existingAccount = new BankAccount();
        existingAccount.setId(1L);
        existingAccount.setUser(mockUser);
        existingAccount.setStatus(AccountStatus.ACTIVE);

        when(bankAccountRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(existingAccount));
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(existingAccount);

        BankAccount blockedAccount = bankAccountService.blockOrActivateAccount(1L, 1L, false);

        assertEquals(AccountStatus.BLOCKED, blockedAccount.getStatus());
        verify(bankAccountRepository, times(1)).save(existingAccount);
    }

    @Test
    void getAllBankAccounts_ShouldReturnListOfAccounts() {
        BankAccount account1 = new BankAccount();
        account1.setId(1L);

        BankAccount account2 = new BankAccount();
        account2.setId(2L);

        when(bankAccountRepository.findAll()).thenReturn(List.of(account1, account2));

        List<BankAccount> accounts = bankAccountService.getAllBankAccounts();

        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        verify(bankAccountRepository, times(1)).findAll();
    }
}
