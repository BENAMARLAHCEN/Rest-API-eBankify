package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.AccountStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found."));
        bankAccount.setUser(user);
        bankAccount.setStatus(AccountStatus.ACTIVE);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, Long userId) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found or not owned by user."));
        existingAccount.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(existingAccount);
    }

    @Override
    public void deleteBankAccount(Long accountId, Long userId) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found or not owned by user."));
        bankAccountRepository.delete(existingAccount);
    }

    @Override
    public BankAccount getBankAccount(Long accountId, Long userId) {
        return bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found or not owned by user."));
    }

    @Override
    public BankAccount getBankAccountForAdmin(Long accountId) {
        return bankAccountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found."));
    }

    @Override
    public List<BankAccount> getAllBankAccountsForUser(Long userId) {
        return bankAccountRepository.findAllByUserId(userId);
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount blockOrActivateAccount(Long accountId, Long userId, boolean activate) {
        BankAccount account = bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new RuntimeException("Account not found for the specified user."));
        account.setStatus(activate ? AccountStatus.ACTIVE : AccountStatus.BLOCKED);
        return bankAccountRepository.save(account);
    }
}