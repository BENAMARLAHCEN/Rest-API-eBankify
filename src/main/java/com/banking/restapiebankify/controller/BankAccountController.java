package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.BankAccountDTO;
import com.banking.restapiebankify.mapper.BankAccountMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.service.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bankaccounts")
public class BankAccountController {

    private final BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    // Utility to get the current username
    private String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    // Create a new bank account (User Only)
    @PostMapping("/create")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankAccountDTO> createBankAccount(@RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = BankAccountMapper.INSTANCE.toBankAccount(bankAccountDTO);
        BankAccount createdAccount = bankAccountService.createBankAccount(bankAccount, getCurrentUsername());
        BankAccountDTO createdAccountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(createdAccount);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAccountDTO);
    }

    // Update an existing bank account (User Only)
    @PutMapping("/update/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BankAccountDTO> updateBankAccount(@PathVariable Long accountId,
                                                            @RequestBody BankAccountDTO bankAccountDTO) {
        BankAccount bankAccount = BankAccountMapper.INSTANCE.toBankAccount(bankAccountDTO);
        BankAccount updatedAccount = bankAccountService.updateBankAccount(accountId, bankAccount, getCurrentUsername());
        BankAccountDTO updatedAccountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(updatedAccount);
        return ResponseEntity.ok(updatedAccountDTO);
    }

    // Delete a bank account (User Only)
    @DeleteMapping("/delete/{accountId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteBankAccount(@PathVariable Long accountId) {
        bankAccountService.deleteBankAccount(accountId, getCurrentUsername());
        return ResponseEntity.ok().build();
    }

    // Retrieve a single bank account (User, Employee, or Admin)
    @GetMapping("/account/{accountId}")
    @PreAuthorize("hasAnyRole('USER', 'EMPLOYEE', 'ADMIN')")
    public ResponseEntity<BankAccountDTO> getBankAccount(@PathVariable Long accountId) {
        BankAccount account = bankAccountService.getBankAccountForUserOrAdmin(accountId, getCurrentUsername());
        BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(account);
        return ResponseEntity.ok(accountDTO);
    }

    @GetMapping("/myAccounts")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BankAccountDTO>> getBankAccountsForUser() {
        List<BankAccount> accounts = bankAccountService.getBankAccountsForUser(getCurrentUsername());
        List<BankAccountDTO> accountDTOs = accounts.stream()
                .map(BankAccountMapper.INSTANCE::toBankAccountDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOs);
    }

    // Retrieve all bank accounts (Admin or Employee Only)
    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<List<BankAccountDTO>> getAllBankAccounts() {
        List<BankAccount> accounts = bankAccountService.getAllBankAccounts();
        List<BankAccountDTO> accountDTOs = accounts.stream()
                .map(BankAccountMapper.INSTANCE::toBankAccountDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(accountDTOs);
    }

    // Block a user's bank account (Admin Only)
    @PatchMapping("/admin/block/{userId}/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankAccountDTO> blockBankAccount(@PathVariable Long userId,
                                                           @PathVariable Long accountId) {
        BankAccount blockedAccount = bankAccountService.blockOrActivateAccount(accountId, userId, false);
        BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(blockedAccount);
        return ResponseEntity.ok(accountDTO);
    }

    // Activate a user's bank account (Admin Only)
    @PatchMapping("/admin/activate/{userId}/{accountId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<BankAccountDTO> activateBankAccount(@PathVariable Long userId,
                                                              @PathVariable Long accountId) {
        BankAccount activatedAccount = bankAccountService.blockOrActivateAccount(accountId, userId, true);
        BankAccountDTO accountDTO = BankAccountMapper.INSTANCE.toBankAccountDTO(activatedAccount);
        return ResponseEntity.ok(accountDTO);
    }
}
