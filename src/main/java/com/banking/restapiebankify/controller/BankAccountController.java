package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.BankAccountService;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BankAccountController(BankAccountService bankAccountService, UserService userService, JwtUtil jwtUtil) {
        this.bankAccountService = bankAccountService;
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
    public ResponseEntity<BankAccount> createBankAccount(@RequestHeader("Authorization") String token, @RequestBody BankAccount bankAccount) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount, user.getId());
            return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<BankAccount> updateBankAccount(@RequestHeader("Authorization") String token, @PathVariable Long accountId, @RequestBody BankAccount bankAccount) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            BankAccount updatedAccount = bankAccountService.updateBankAccount(accountId, bankAccount, user.getId());
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(@RequestHeader("Authorization") String token, @PathVariable Long accountId) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            bankAccountService.deleteBankAccount(accountId, user.getId());
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<BankAccount> getBankAccount(@RequestHeader("Authorization") String token, @PathVariable Long accountId) {
        User user = getUserFromToken(token);
        if (user != null) {
            BankAccount account = bankAccountService.getBankAccount(accountId, user.getId());
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else if (user.getRole().getName().equals("ADMIN")) {
            BankAccount account = bankAccountService.getBankAccountForAdmin(accountId);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankAccount>> getAllBankAccounts(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
            return new ResponseEntity<>(accounts, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/admin/block/{userId}/{accountId}")
    public ResponseEntity<BankAccount> blockAccount(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Long accountId) {
        User adminUser = getUserFromToken(token);
        if (adminUser.getRole().getName().equals("ADMIN")) {
            BankAccount account = bankAccountService.blockOrActivateAccount(accountId, userId, false);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/admin/activate/{userId}/{accountId}")
    public ResponseEntity<BankAccount> activateAccount(@RequestHeader("Authorization") String token, @PathVariable Long userId, @PathVariable Long accountId) {
        User adminUser = getUserFromToken(token);
        if (adminUser.getRole().getName().equals("ADMIN")) {
            BankAccount account = bankAccountService.blockOrActivateAccount(accountId, userId, true);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}