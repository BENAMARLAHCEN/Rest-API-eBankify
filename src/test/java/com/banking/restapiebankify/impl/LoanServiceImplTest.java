package com.banking.restapiebankify.impl;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.LoanStatus;
import com.banking.restapiebankify.repository.LoanRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.impl.LoanServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanServiceImplTest {

    @Mock
    private LoanRepository loanRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoanServiceImpl loanService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void requestLoan_ShouldReturnLoan() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setBirthday(LocalDate.now().minusYears(25));

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setAmount(BigDecimal.valueOf(5000));
        loanDTO.setInterestRate(BigDecimal.valueOf(5.0));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));
        when(loanRepository.save(any(Loan.class))).thenAnswer(invocation -> {
            Loan savedLoan = invocation.getArgument(0);
            savedLoan.setId(1L);
            return savedLoan;
        });

        Loan loan = loanService.requestLoan(loanDTO, "testuser");

        assertNotNull(loan);
        assertEquals(1L, loan.getId());
        assertEquals(LoanStatus.PENDING, loan.getStatus());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void requestLoan_ShouldThrowException_WhenUserIsUnderage() {
        User mockUser = new User();
        mockUser.setId(1L);
        mockUser.setUsername("testuser");
        mockUser.setBirthday(LocalDate.now().minusYears(17));

        LoanDTO loanDTO = new LoanDTO();
        loanDTO.setAmount(BigDecimal.valueOf(5000));

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(mockUser));

        assertThrows(RuntimeException.class, () -> loanService.requestLoan(loanDTO, "testuser"));
        verify(loanRepository, never()).save(any(Loan.class));
    }

    @Test
    void approveLoan_ShouldSetStatusToApproved() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan approvedLoan = loanService.approveLoan(1L, "employeeUser");

        assertEquals(LoanStatus.APPROVED, approvedLoan.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void rejectLoan_ShouldSetStatusToRejected() {
        Loan loan = new Loan();
        loan.setId(1L);
        loan.setStatus(LoanStatus.PENDING);

        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);

        Loan rejectedLoan = loanService.rejectLoan(1L, "employeeUser", "Insufficient collateral");

        assertEquals(LoanStatus.REJECTED, rejectedLoan.getStatus());
        verify(loanRepository, times(1)).save(loan);
    }

    @Test
    void getAllLoans_ShouldReturnAllLoans() {
        Loan loan1 = new Loan();
        loan1.setId(1L);
        Loan loan2 = new Loan();
        loan2.setId(2L);

        when(loanRepository.findAll()).thenReturn(List.of(loan1, loan2));

        Page<Loan> loans = loanService.getAllLoans(Pageable.unpaged());

        assertNotNull(loans);
        assertEquals(2, loans.getTotalElements());
        verify(loanRepository, times(1)).findAll();
    }
}