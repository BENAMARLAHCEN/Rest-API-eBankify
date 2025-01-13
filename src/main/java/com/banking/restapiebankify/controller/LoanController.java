package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.mapper.LoanMapper;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.service.LoanService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
        Loan loan = loanService.requestLoan(loanDTO, getCurrentUserUsername());
        return ResponseEntity.status(201).body(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @PatchMapping("/approve/{loanId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoanDTO> approveLoan(@PathVariable Long loanId) {
        Loan loan = loanService.approveLoan(loanId, getCurrentUserUsername());
        return ResponseEntity.ok(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @PatchMapping("/reject/{loanId}")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<LoanDTO> rejectLoan(@PathVariable Long loanId, @RequestParam String remarks) {
        Loan loan = loanService.rejectLoan(loanId, getCurrentUserUsername(), remarks);
        return ResponseEntity.ok(LoanMapper.INSTANCE.toLoanDTO(loan));
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<LoanDTO>> getLoansForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Loan> loans = loanService.getLoansByUser(getCurrentUserUsername(), pageable);
        Page<LoanDTO> loanDTOs = loans.map(LoanMapper.INSTANCE::toLoanDTO);
        return ResponseEntity.ok(loanDTOs);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('EMPLOYEE', 'ADMIN')")
    public ResponseEntity<Page<LoanDTO>> getAllLoans(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Loan> loans = loanService.getAllLoans(pageable);
        Page<LoanDTO> loanDTOs = loans.map(LoanMapper.INSTANCE::toLoanDTO);
        return ResponseEntity.ok(loanDTOs);
    }

    private String getCurrentUserUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
    }
}