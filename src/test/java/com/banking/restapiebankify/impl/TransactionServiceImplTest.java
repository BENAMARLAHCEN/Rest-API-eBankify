package com.banking.restapiebankify.impl;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.TransactionStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.TransactionRepository;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTransaction_ShouldReturnTransaction() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(2000));
        fromAccount.setUser(mockUser);

        BankAccount toAccount = new BankAccount();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(1000));

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountId(1L);
        transactionDTO.setToAccountId(2L);
        transactionDTO.setAmount(BigDecimal.valueOf(500));

        when(userService.findUserByUsername("testUser")).thenReturn(mockUser);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(toAccount));
        when(transactionRepository.save(any(Transaction.class))).thenAnswer(invocation -> {
            Transaction savedTransaction = invocation.getArgument(0);
            savedTransaction.setId(1L);
            return savedTransaction;
        });

        Transaction transaction = transactionService.createTransaction(transactionDTO, "testUser");

        assertNotNull(transaction);
        assertEquals(1L, transaction.getId());
        assertEquals(BigDecimal.valueOf(500), transaction.getAmount());
        verify(bankAccountRepository, times(1)).save(fromAccount);
        verify(bankAccountRepository, times(1)).save(toAccount);
    }

    @Test
    void createTransaction_ShouldThrowException_WhenInsufficientBalance() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testUser");

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(100));
        fromAccount.setUser(mockUser);

        BankAccount toAccount = new BankAccount();
        toAccount.setId(2L);

        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setFromAccountId(1L);
        transactionDTO.setToAccountId(2L);
        transactionDTO.setAmount(BigDecimal.valueOf(500));

        when(userService.findUserByUsername("testUser")).thenReturn(mockUser);
        when(bankAccountRepository.findById(1L)).thenReturn(Optional.of(fromAccount));
        when(bankAccountRepository.findById(2L)).thenReturn(Optional.of(toAccount));

        assertThrows(RuntimeException.class, () -> transactionService.createTransaction(transactionDTO, "testUser"));
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    @Test
    void approveTransaction_ShouldCompleteTransaction() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setAmount(BigDecimal.valueOf(500));
        transaction.setStatus(TransactionStatus.PENDING);

        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(1L);
        fromAccount.setBalance(BigDecimal.valueOf(1000));

        BankAccount toAccount = new BankAccount();
        toAccount.setId(2L);
        toAccount.setBalance(BigDecimal.valueOf(500));

        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction approvedTransaction = transactionService.approveTransaction(1L, "adminUser");

        assertEquals(TransactionStatus.COMPLETED, approvedTransaction.getStatus());
        verify(bankAccountRepository, times(1)).save(fromAccount);
        verify(bankAccountRepository, times(1)).save(toAccount);
    }

    @Test
    void rejectTransaction_ShouldSetStatusToRejected() {
        Transaction transaction = new Transaction();
        transaction.setId(1L);
        transaction.setStatus(TransactionStatus.PENDING);

        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        Transaction rejectedTransaction = transactionService.rejectTransaction(1L, "adminUser", "Invalid details");

        assertEquals(TransactionStatus.REJECTED, rejectedTransaction.getStatus());
        assertEquals("Invalid details", rejectedTransaction.getRemarks());
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void getTransactionsForAccount_ShouldReturnTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);

        when(transactionRepository.findByFromAccountId(1L)).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getTransactionsForAccount(1L, "testUser");

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findByFromAccountId(1L);
    }

    @Test
    void getAllTransactions_ShouldReturnAllTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setId(1L);

        Transaction transaction2 = new Transaction();
        transaction2.setId(2L);

        when(transactionRepository.findAll()).thenReturn(List.of(transaction1, transaction2));

        List<Transaction> transactions = transactionService.getAllTransactions();

        assertEquals(2, transactions.size());
        verify(transactionRepository, times(1)).findAll();
    }
}
