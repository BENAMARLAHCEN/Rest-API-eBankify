package com.banking.restapiebankify.service;

import com.banking.restapiebankify.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccount bankAccount, String username);
    BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, String username);
    void deleteBankAccount(Long accountId, String username);
    BankAccount getBankAccountForUserOrAdmin(Long accountId, String username);
    List<BankAccount> getAllBankAccounts();
    BankAccount blockOrActivateAccount(Long accountId, Long userId, boolean activate);


    List<BankAccount> getBankAccountsForUser(String currentUsername);
}
