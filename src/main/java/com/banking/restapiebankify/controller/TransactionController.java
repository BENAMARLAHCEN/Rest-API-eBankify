package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.mapper.TransactionMapper;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<TransactionDTO> createTransaction(@RequestBody TransactionDTO transactionDTO) {
        Transaction transaction = transactionService.createTransaction(transactionDTO, getCurrentUsername());
        return ResponseEntity.status(201).body(TransactionMapper.INSTANCE.toTransactionDTO(transaction));
    }

    @PatchMapping("/approve/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TransactionDTO> approveTransaction(@PathVariable Long transactionId) {
        Transaction transaction = transactionService.approveTransaction(transactionId, getCurrentUsername());
        return ResponseEntity.ok(TransactionMapper.INSTANCE.toTransactionDTO(transaction));
    }

    @PatchMapping("/reject/{transactionId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<TransactionDTO> rejectTransaction(@PathVariable Long transactionId,
                                                            @RequestParam String remarks) {
        Transaction transaction = transactionService.rejectTransaction(transactionId, getCurrentUsername(), remarks);
        return ResponseEntity.ok(TransactionMapper.INSTANCE.toTransactionDTO(transaction));
    }

    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<TransactionDTO>> getTransactionsForAccount(@PathVariable Long accountId) {
        List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId, getCurrentUsername());
        List<TransactionDTO> transactionDTOs = List.copyOf(transactions.stream()
                .map(TransactionMapper.INSTANCE::toTransactionDTO)
                .toList());
        return ResponseEntity.ok(transactionDTOs);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        List<TransactionDTO> transactionDTOs = List.copyOf(transactions.stream()
                .map(TransactionMapper.INSTANCE::toTransactionDTO)
                .toList());
        return ResponseEntity.ok(transactionDTOs);
    }

    private String getCurrentUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
