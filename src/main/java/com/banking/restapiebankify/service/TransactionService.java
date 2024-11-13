package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.TransactionDTO;
import com.banking.restapiebankify.model.Transaction;
import com.banking.restapiebankify.model.enums.TransactionStatus;

import java.util.List;

public interface TransactionService {
    Transaction createTransaction(TransactionDTO transactionDTO, Long userId);
    Transaction approveTransaction(Long transactionId, Long employeeId);
    Transaction rejectTransaction(Long transactionId, Long employeeId, String remarks);
    List<Transaction> getTransactionsForAccount(Long accountId, Long userId);
    List<Transaction> getAllTransactions();
}
