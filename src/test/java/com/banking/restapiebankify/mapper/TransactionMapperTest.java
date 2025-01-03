package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.enums.TransactionStatus;
import com.banking.restapiebankify.model.enums.TransactionType;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class TransactionMapperTest {

    private final TransactionMapper mapper = TransactionMapper.INSTANCE;

    @Test
    void toTransactionDTO_ShouldMapTransactionToDTO() {
        BankAccount fromAccount = new BankAccount();
        fromAccount.setId(1L);

        BankAccount toAccount = new BankAccount();
        toAccount.setId(2L);

        Transaction transaction = Transaction.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .type(TransactionType.CLASSIC)
                .status(TransactionStatus.PENDING)
                .fromAccount(fromAccount)
                .toAccount(toAccount)
                .timestamp(LocalDateTime.of(2023, 12, 25, 12, 0))
                .frequency("DAILY")
                .startDate(LocalDateTime.of(2023, 12, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .build();

        TransactionDTO dto = mapper.toTransactionDTO(transaction);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(BigDecimal.valueOf(1000), dto.getAmount());
        assertEquals("CLASSIC", dto.getType());
        assertEquals("PENDING", dto.getStatus());
        assertEquals(1L, dto.getFromAccountId());
        assertEquals(2L, dto.getToAccountId());
        assertEquals(LocalDateTime.of(2023, 12, 25, 12, 0), dto.getTimestamp());
        assertEquals("DAILY", dto.getFrequency());
        assertEquals(LocalDateTime.of(2023, 12, 1, 0, 0), dto.getStartDate());
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), dto.getEndDate());
    }

    @Test
    void toTransaction_ShouldMapDTOToTransaction() {
        TransactionDTO dto = TransactionDTO.builder()
                .id(1L)
                .amount(BigDecimal.valueOf(1000))
                .type("CLASSIC")
                .status("PENDING")
                .fromAccountId(1L)
                .toAccountId(2L)
                .timestamp(LocalDateTime.of(2023, 12, 25, 12, 0))
                .frequency("DAILY")
                .startDate(LocalDateTime.of(2023, 12, 1, 0, 0))
                .endDate(LocalDateTime.of(2023, 12, 31, 23, 59))
                .build();

        Transaction transaction = mapper.toTransaction(dto);

        assertNotNull(transaction);
        assertEquals(1L, transaction.getId());
        assertEquals(BigDecimal.valueOf(1000), transaction.getAmount());
        assertEquals(TransactionType.CLASSIC, transaction.getType());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertNotNull(transaction.getFromAccount());
        assertEquals(1L, transaction.getFromAccount().getId());
        assertNotNull(transaction.getToAccount());
        assertEquals(2L, transaction.getToAccount().getId());
        assertEquals(LocalDateTime.of(2023, 12, 25, 12, 0), transaction.getTimestamp());
        assertEquals("DAILY", transaction.getFrequency());
        assertEquals(LocalDateTime.of(2023, 12, 1, 0, 0), transaction.getStartDate());
        assertEquals(LocalDateTime.of(2023, 12, 31, 23, 59), transaction.getEndDate());
    }

    @Test
    void toTransactionDTO_ShouldHandleNullTransaction() {
        TransactionDTO dto = mapper.toTransactionDTO(null);
        assertNull(dto);
    }

    @Test
    void toTransaction_ShouldHandleNullDTO() {
        Transaction transaction = mapper.toTransaction(null);
        assertNull(transaction);
    }
}