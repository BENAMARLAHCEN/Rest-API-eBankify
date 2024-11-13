package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.mapper.TransactionMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.TransactionStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.TransactionRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.BankAccountService;
import com.banking.restapiebankify.service.TransactionService;
import com.banking.restapiebankify.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Transaction createTransaction(TransactionDTO transactionDTO, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionDTO);
        BankAccount fromAccount = bankAccountRepository.findById(transactionDTO.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("from Account not found"));
        BankAccount toAccount = bankAccountRepository.findById(transactionDTO.getToAccountId())
                .orElseThrow(() -> new RuntimeException("to Account not found"));
        if (fromAccount.getBalance().compareTo(transaction.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }
        if (fromAccount.getId().equals(toAccount.getId())) {
            throw new RuntimeException("Cannot transfer to the same account");
        }
        if (!fromAccount.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("You are not authorized to perform this transaction");
        }

        if (transactionDTO.getType().equals("STANDING_ORDER")) {
            transaction.setStatus(TransactionStatus.PENDING);
            transaction.setTimestamp(LocalDateTime.now());
            return transactionRepository.save(transaction);
        }

        if (transaction.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            transaction.setStatus(TransactionStatus.PENDING);
        } else {
            transaction.setStatus(TransactionStatus.COMPLETED);
            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            bankAccountRepository.save(fromAccount);
            bankAccountRepository.save(toAccount);
        }
        transaction.setTimestamp(LocalDateTime.now());
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction approveTransaction(Long transactionId, Long employeeId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(TransactionStatus.COMPLETED);
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        transaction.setApprovedBy(employee);
        if (transaction.getType().equals("STANDING_ORDER")) {
            transaction.setTimestamp(LocalDateTime.now());
        } else {
            BankAccount fromAccount = transaction.getFromAccount();
            BankAccount toAccount = transaction.getToAccount();
            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            bankAccountRepository.save(fromAccount);
            bankAccountRepository.save(toAccount);
        }
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction rejectTransaction(Long transactionId, Long employeeId, String remarks) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new RuntimeException("Transaction not found"));

        transaction.setStatus(TransactionStatus.REJECTED);
        User employee = userRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        transaction.setApprovedBy(employee);
        transaction.setRemarks(remarks);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId, Long userId) {
        return transactionRepository.findByFromAccountId(accountId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
