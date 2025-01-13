package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.mapper.LoanMapper;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.LoanStatus;
import com.banking.restapiebankify.repository.LoanRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserRepository userRepository;

    public LoanServiceImpl(LoanRepository loanRepository, UserRepository userRepository) {
        this.loanRepository = loanRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Loan requestLoan(LoanDTO loanDTO, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (user.getBirthday().plusYears(18).isAfter(LocalDate.now())) {
            throw new RuntimeException("User is not eligible for a loan: must be at least 18 years old");
        }
        if (loanDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Loan amount must be greater than 0");
        }

        Loan loan = LoanMapper.INSTANCE.toLoan(loanDTO);
        loan.setUser(user);
        loan.setStatus(LoanStatus.PENDING);
        return loanRepository.save(loan);
    }

    @Override
    public Loan approveLoan(Long loanId, String employeeUsername) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.APPROVED);
        return loanRepository.save(loan);
    }

    @Override
    public Loan rejectLoan(Long loanId, String employeeUsername, String remarks) {
        Loan loan = loanRepository.findById(loanId)
                .orElseThrow(() -> new RuntimeException("Loan not found"));
        loan.setStatus(LoanStatus.REJECTED);
        return loanRepository.save(loan);
    }

    @Override
    public Page<Loan> getLoansByUser(String username, Pageable pageable) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return loanRepository.findByUserId(user.getId(), pageable);
    }

    @Override
    public Page<Loan> getAllLoans(Pageable pageable) {
        return loanRepository.findAll(pageable);
    }
}