package com.banking.restapiebankify.service;

import com.banking.restapiebankify.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccount bankAccount, String username);
    BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, String username);
    void deleteBankAccount(Long accountId, String username);
    BankAccount getBankAccountForUserOrAdmin(Long accountId, String username);
    Page<BankAccount> getAllBankAccounts(Pageable pageable);
    BankAccount blockOrActivateAccount(Long accountId, boolean activate);
    Page<BankAccount> getBankAccountsForUser(String currentUsername, Pageable pageable);
}
