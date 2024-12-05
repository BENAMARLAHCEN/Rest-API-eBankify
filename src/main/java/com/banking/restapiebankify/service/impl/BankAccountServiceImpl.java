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
                .orElseThrow(() -> new RuntimeException("User not found."));
        bankAccount.setUser(user);
        bankAccount.setAccountNumber(UUID.randomUUID().toString());
        bankAccount.setStatus(AccountStatus.ACTIVE);
        return bankAccountRepository.save(bankAccount);
    }

    @Override
    public BankAccount updateBankAccount(Long accountId, BankAccount bankAccount, String username) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found.")).getId())
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found or not owned by user."));
        existingAccount.setBalance(bankAccount.getBalance());
        return bankAccountRepository.save(existingAccount);
    }

    @Override
    public void deleteBankAccount(Long accountId, String username) {
        BankAccount existingAccount = bankAccountRepository.findByIdAndUserId(accountId, userRepository.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found.")).getId())
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found or not owned by user."));
        bankAccountRepository.delete(existingAccount);
    }

    @Override
    public BankAccount getBankAccountForUserOrAdmin(Long accountId, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found."));
        if (user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            return bankAccountRepository.findById(accountId)
                    .orElseThrow(() -> new BankAccountNotFoundException("Account not found."));
        }
        return bankAccountRepository.findByIdAndUserId(accountId, user.getId())
                .orElseThrow(() -> new BankAccountNotFoundException("Account not found or not owned by user."));
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
                .orElseThrow(() -> new RuntimeException("User not found."));
        return bankAccountRepository.findByUserId(user.getId());
    }
}
