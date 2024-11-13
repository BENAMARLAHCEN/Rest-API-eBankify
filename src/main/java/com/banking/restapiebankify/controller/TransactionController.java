package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.TransactionService;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public TransactionController(TransactionService transactionService, UserService userService, JwtUtil jwtUtil) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private User getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        return userService.findUserByUsername(username);
    }

    @PostMapping("/create")
    public ResponseEntity<Transaction> createTransaction(@RequestHeader("Authorization") String token, @RequestBody TransactionDTO transactionDTO) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            Transaction createdTransaction = transactionService.createTransaction(transactionDTO, user.getId());
            return new ResponseEntity<>(createdTransaction, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/approve/{transactionId}")
    public ResponseEntity<Transaction> approveTransaction(@RequestHeader("Authorization") String token, @PathVariable Long transactionId) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            Transaction approvedTransaction = transactionService.approveTransaction(transactionId, user.getId());
            return new ResponseEntity<>(approvedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/reject/{transactionId}")
    public ResponseEntity<Transaction> rejectTransaction(@RequestHeader("Authorization") String token, @PathVariable Long transactionId, @RequestParam String remarks) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            Transaction rejectedTransaction = transactionService.rejectTransaction(transactionId, user.getId(), remarks);
            return new ResponseEntity<>(rejectedTransaction, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@RequestHeader("Authorization") String token, @PathVariable Long accountId) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER") || user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            if (user.getRole().getName().equals("USER") && !user.getAccounts().stream().anyMatch(account -> account.getId().equals(accountId))) {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
            List<Transaction> transactions = transactionService.getTransactionsForAccount(accountId, user.getId());
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<Transaction>> getAllTransactions(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            List<Transaction> transactions = transactionService.getAllTransactions();
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
