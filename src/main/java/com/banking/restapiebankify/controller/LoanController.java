package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.mapper.LoanMapper;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.service.LoanService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
    }

    @PostMapping("/request")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<LoanDTO> requestLoan(@RequestBody LoanDTO loanDTO) {
        Loan loan = loanService.requestLoan(loanDTO, getCurrentUserId());
        return ResponseEntity.status(201).body(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @PatchMapping("/approve/{loanId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoanDTO> approveLoan(@PathVariable Long loanId) {
        Loan loan = loanService.approveLoan(loanId, getCurrentUserId());
        return ResponseEntity.ok(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @PatchMapping("/reject/{loanId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoanDTO> rejectLoan(@PathVariable Long loanId, @RequestParam String remarks) {
        Loan loan = loanService.rejectLoan(loanId, getCurrentUserId(), remarks);
        return ResponseEntity.ok(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<LoanDTO>> getLoansForUser() {
        List<Loan> loans = loanService.getLoansByUser(getCurrentUserId());
        List<LoanDTO> loanDTOs = loans.stream()
                .map(LoanMapper.INSTANCE::toLoanDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDTOs);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<List<LoanDTO>> getAllLoans() {
        List<Loan> loans = loanService.getAllLoans();
        List<LoanDTO> loanDTOs = loans.stream()
                .map(LoanMapper.INSTANCE::toLoanDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(loanDTOs);
    }

    private Long getCurrentUserId() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(username); // Adjust logic if `username` is not the user ID
    }
}
