package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.exception.BankAccountNotFoundException;
import com.banking.restapiebankify.exception.TransactionNotFoundException;
import com.banking.restapiebankify.exception.UnauthorizedTransactionException;
import com.banking.restapiebankify.mapper.TransactionMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.TransactionStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.TransactionRepository;
import com.banking.restapiebankify.service.TransactionService;
import com.banking.restapiebankify.service.UserService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BankAccountRepository bankAccountRepository;
    private final UserService userService;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  BankAccountRepository bankAccountRepository,
                                  UserService userService) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.userService = userService;
    }

    @Override
    public Transaction createTransaction(TransactionDTO transactionDTO, String username) {
        User user = userService.findUserByUsername(username);
        BankAccount fromAccount = bankAccountRepository.findById(transactionDTO.getFromAccountId())
                .orElseThrow(() -> new BankAccountNotFoundException("From account not found"));
        BankAccount toAccount = bankAccountRepository.findById(transactionDTO.getToAccountId())
                .orElseThrow(() -> new BankAccountNotFoundException("To account not found"));

        if (!fromAccount.getUser().equals(user)) {
            throw new UnauthorizedTransactionException("Unauthorized transaction");
        }
        if (fromAccount.getBalance().compareTo(transactionDTO.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        Transaction transaction = TransactionMapper.INSTANCE.toTransaction(transactionDTO);
        transaction.setFromAccount(fromAccount);
        transaction.setToAccount(toAccount);
        transaction.setTimestamp(LocalDateTime.now());

        if (transaction.getAmount().compareTo(new BigDecimal("10000")) > 0) {
            transaction.setStatus(TransactionStatus.PENDING);
        } else {
            transaction.setStatus(TransactionStatus.COMPLETED);
            fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
            toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
            bankAccountRepository.save(fromAccount);
            bankAccountRepository.save(toAccount);
        }

        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction approveTransaction(Long transactionId, String username) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setTimestamp(LocalDateTime.now());
        BankAccount fromAccount = transaction.getFromAccount();
        BankAccount toAccount = transaction.getToAccount();
        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));
        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction rejectTransaction(Long transactionId, String username, String remarks) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found"));
        transaction.setStatus(TransactionStatus.REJECTED);
        transaction.setRemarks(remarks);
        return transactionRepository.save(transaction);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId, String username) {
        return transactionRepository.findByFromAccountId(accountId);
    }

    @Override
    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }
}