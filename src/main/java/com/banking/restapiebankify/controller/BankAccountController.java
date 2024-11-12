package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.BankAccountDTO;
import com.banking.restapiebankify.mapper.BankAccountMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.BankAccountService;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestHeader("Authorization") String token,
                                                            @RequestBody BankAccountDTO bankAccountDTO) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            BankAccount bankAccount = BankAccountMapper.INSTANCE.toBankAccount(bankAccountDTO);
            BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount, user.getId());
            BankAccountDTO createdAccountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(createdAccount);
            return new ResponseEntity<>(createdAccountDTO, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/update/{accountId}")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@RequestHeader("Authorization") String token,
                                                            @PathVariable Long accountId,
                                                            @RequestBody BankAccountDTO bankAccountDTO) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            BankAccount bankAccount = BankAccountMapper.INSTANCE.toBankAccount(bankAccountDTO);
            BankAccount updatedAccount = bankAccountService.updateBankAccount(accountId, bankAccount, user.getId());
            BankAccountDTO updatedAccountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(updatedAccount);
            return new ResponseEntity<>(updatedAccountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/delete/{accountId}")
    public ResponseEntity<Void> deleteBankAccount(@RequestHeader("Authorization") String token,
                                                  @PathVariable Long accountId) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("USER")) {
            bankAccountService.deleteBankAccount(accountId, user.getId());
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<BankAccountDTO> getBankAccount(@RequestHeader("Authorization") String token,
                                                         @PathVariable Long accountId) {
        User user = getUserFromToken(token);
        BankAccount account;
        if (user.getRole().getName().equals("USER")) {
            account = bankAccountService.getBankAccount(accountId, user.getId());
        } else if (user.getRole().getName().equals("EMPLOYEE") || user.getRole().getName().equals("ADMIN")) {
            account = bankAccountService.getBankAccountForAdmin(accountId);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(account);
        return new ResponseEntity<>(accountDTO, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
            List<BankAccountDTO> accountDTOs = accounts.stream()
                    .map(BankAccountMapper.INSTANCE::toBankAccountDTO)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(accountDTOs, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/admin/block/{userId}/{accountId}")
    public ResponseEntity<BankAccountDTO> blockAccount(@RequestHeader("Authorization") String token,
                                                       @PathVariable Long userId,
                                                       @PathVariable Long accountId) {
        User adminUser = getUserFromToken(token);
        if (adminUser.getRole().getName().equals("ADMIN")) {
            BankAccount account = bankAccountService.blockOrActivateAccount(accountId, userId, false);
            BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(account);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PatchMapping("/admin/activate/{userId}/{accountId}")
    public ResponseEntity<BankAccountDTO> activateAccount(@RequestHeader("Authorization") String token,
                                                          @PathVariable Long userId,
                                                          @PathVariable Long accountId) {
        User adminUser = getUserFromToken(token);
        if (adminUser.getRole().getName().equals("ADMIN")) {
            BankAccount account = bankAccountService.blockOrActivateAccount(accountId, userId, true);
            BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(account);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
