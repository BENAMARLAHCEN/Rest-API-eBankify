package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LoanService {
    Loan requestLoan(LoanDTO loanDTO, String username);
    Loan approveLoan(Long loanId, String employeeUsername);
    Loan rejectLoan(Long loanId, String employeeUsername, String remarks);
    Page<Loan> getLoansByUser(String username, Pageable pageable);
    Page<Loan> getAllLoans(Pageable pageable);
}