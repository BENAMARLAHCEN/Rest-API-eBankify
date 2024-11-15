package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.model.User;

import java.util.List;

public interface LoanService {
    Loan requestLoan(LoanDTO loanDTO, User user);
    Loan approveLoan(Long loanId, Long employeeId);
    Loan rejectLoan(Long loanId, Long employeeId, String remarks);
    List<Loan> getLoansByUser(Long userId);
    List<Loan> getAllLoans();
}