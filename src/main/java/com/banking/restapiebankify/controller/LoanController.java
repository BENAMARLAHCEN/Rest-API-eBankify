package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.LoanDTO;
import com.banking.restapiebankify.model.Loan;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.LoanService;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public LoanController(LoanService loanService, UserService userService, JwtUtil jwtUtil) {
        this.loanService = loanService;
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

    @PostMapping("/request")
    public ResponseEntity<Loan> requestLoan(@RequestHeader("Authorization") String token, @RequestBody LoanDTO loanDTO) {
        User user = getUserFromToken(token);
        Loan requestedLoan = loanService.requestLoan(loanDTO, user);
        return new ResponseEntity<>(requestedLoan, HttpStatus.CREATED);
    }

    @PatchMapping("/approve/{loanId}")
    public ResponseEntity<Loan> approveLoan(@RequestHeader("Authorization") String token, @PathVariable Long loanId) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            Loan approvedLoan = loanService.approveLoan(loanId, user.getId());
            return new ResponseEntity<>(approvedLoan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/reject/{loanId}")
    public ResponseEntity<Loan> rejectLoan(@RequestHeader("Authorization") String token, @PathVariable Long loanId, @RequestParam String remarks) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            Loan rejectedLoan = loanService.rejectLoan(loanId, user.getId(), remarks);
            return new ResponseEntity<>(rejectedLoan, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/user")
    public ResponseEntity<List<Loan>> getLoansForUser(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        List<Loan> loans = loanService.getLoansByUser(user.getId());
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Loan>> getAllLoans(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            List<Loan> loans = loanService.getAllLoans();
            return new ResponseEntity<>(loans, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}