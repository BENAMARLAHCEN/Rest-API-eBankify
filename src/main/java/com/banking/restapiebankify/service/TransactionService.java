package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionDTO transactionDTO, String username);
    Transaction approveTransaction(Long transactionId, String username);
    Transaction rejectTransaction(Long transactionId, String username, String remarks);
    Page<Transaction> getTransactionsForAccount(Long accountId, String username, Pageable pageable);
    Page<Transaction> getAllTransactions(Pageable pageable);

    Page<Transaction> getTransactionsForUser(String currentUsername, Pageable pageable);
}
