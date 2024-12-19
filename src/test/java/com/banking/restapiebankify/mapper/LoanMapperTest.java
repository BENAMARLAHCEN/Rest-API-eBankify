package com.banking.restapiebankify.mapper;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.LoanStatus;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class LoanMapperTest {

    private final LoanMapper mapper = LoanMapper.INSTANCE;

    @Test
    void toLoanDTO_ShouldMapLoanToDTO() {
        User user = new User();
        user.setId(1L);

        Loan loan = Loan.builder()
                .id(1L)
                .user(user)
                .amount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(5.0))
                .loanStartDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .loanEndDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .status(LoanStatus.PENDING)
                .build();

        LoanDTO dto = mapper.toLoanDTO(loan);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals(1L, dto.getUserId());
        assertEquals(BigDecimal.valueOf(5000), dto.getAmount());
        assertEquals(BigDecimal.valueOf(5.0), dto.getInterestRate());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), dto.getLoanStartDate());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), dto.getLoanEndDate());
        assertEquals("PENDING", dto.getStatus());
    }

    @Test
    void toLoan_ShouldMapDTOToLoan() {
        LoanDTO dto = LoanDTO.builder()
                .id(1L)
                .userId(1L)
                .amount(BigDecimal.valueOf(5000))
                .interestRate(BigDecimal.valueOf(5.0))
                .loanStartDate(LocalDateTime.of(2023, 1, 1, 10, 0))
                .loanEndDate(LocalDateTime.of(2025, 1, 1, 10, 0))
                .status("PENDING")
                .build();

        Loan loan = mapper.toLoan(dto);

        assertNotNull(loan);
        assertEquals(1L, loan.getId());
        assertNotNull(loan.getUser());
        assertEquals(1L, loan.getUser().getId());
        assertEquals(BigDecimal.valueOf(5000), loan.getAmount());
        assertEquals(BigDecimal.valueOf(5.0), loan.getInterestRate());
        assertEquals(LocalDateTime.of(2023, 1, 1, 10, 0), loan.getLoanStartDate());
        assertEquals(LocalDateTime.of(2025, 1, 1, 10, 0), loan.getLoanEndDate());
        assertEquals(LoanStatus.PENDING, loan.getStatus());
    }

    @Test
    void toLoanDTO_ShouldHandleNullLoan() {
        LoanDTO dto = mapper.toLoanDTO(null);
        assertNull(dto);
    }

    @Test
    void toLoan_ShouldHandleNullDTO() {
        Loan loan = mapper.toLoan(null);
        assertNull(loan);
    }
}
