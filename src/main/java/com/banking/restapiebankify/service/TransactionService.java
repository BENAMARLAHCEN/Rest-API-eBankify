package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.Transaction;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionDTO transactionDTO, String username);
    Transaction approveTransaction(Long transactionId, String username);
    Transaction rejectTransaction(Long transactionId, String username, String remarks);
    List<Transaction> getTransactionsForAccount(Long accountId, String username);
    List<Transaction> getAllTransactions();
}
