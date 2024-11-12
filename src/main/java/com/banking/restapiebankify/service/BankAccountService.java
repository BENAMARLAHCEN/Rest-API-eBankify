package com.banking.restapiebankify.service;

import com.banking.restapiebankify.model.BankAccount;

import java.util.List;

public interface BankAccountService {
    BankAccount createBankAccount(BankAccount bankAccount, Long userId);
    BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, Long userId);
    void deleteBankAccount(Long accountId, Long userId);
    BankAccount getBankAccount(Long accountId, Long userId);
    BankAccount getBankAccountForAdmin(Long accountId);
    List<BankAccount> getAllBankAccountsForUser(Long userId);
    List<BankAccount> getAllBankAccounts();
    BankAccount blockOrActivateAccount(Long accountId, Long userId, boolean activate);
}
