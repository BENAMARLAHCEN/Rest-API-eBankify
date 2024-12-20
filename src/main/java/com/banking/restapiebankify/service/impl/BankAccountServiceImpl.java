package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.exception.BankAccountNotFoundException;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.AccountStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.BankAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private static final String USER_NOT_FOUND = "User not found.";
    private static final String ACCOUNT_NOT_FOUND_OR_NOT_OWNED = "Account not found or not owned by user.";

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;

    @Autowired
    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
    }

    @Override
    public BankAccount createBankAccount(BankAccount bankAccount, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        bankAccount.setUser(user);
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setStatus(AccountStatus.ACTIVE);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, String username) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND)).getId())
                .orElseThrow(() -> new BankAccountNotFoundException(ACCOUNT_NOT_FOUND_OR_NOT_OWNED));
        existingAccount.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(existingAccount);
    }

    @Override
    public void deleteBankAccount(Long accountId, String username) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND)).getId())
                .orElseThrow(() -> new BankAccountNotFoundException(ACCOUNT_NOT_FOUND_OR_NOT_OWNED));
        bankAccountRepository.delete(existingAccount);
    }

    @Override
    public BankAccount getBankAccountForUserOrAdmin(Long accountId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            return bankAccountRepository.findById(accountId)
                    .orElseThrow(() -> new BankAccountNotFoundException("Account not found."));
        }
        return bankAccountRepository.findByIdAndUserId(accountId, user.getId())
                .orElseThrow(() -> new BankAccountNotFoundException(ACCOUNT_NOT_FOUND_OR_NOT_OWNED));
    }

    @Override
    public List<BankAccount> getAllBankAccounts() {
        return bankAccountRepository.findAll();
    }

    @Override
    public BankAccount blockOrActivateAccount(Long accountId, Long userId, boolean activate) {
        BankAccount account = bankAccountRepository.findByIdAndUserId(accountId, userId)
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found for the specified user."));
        account.setStatus(activate ? AccountStatus.ACTIVE : AccountStatus.BLOCKED);
        return bankAccountRepository.save(account);
    }

    @Override
    public List<BankAccount> getBankAccountsForUser(String currentUsername) {
        User user = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException(USER_NOT_FOUND));
        return bankAccountRepository.findByUserId(user.getId());
    }
}