package com.banking.restapiebankify.repository;

import com.banking.restapiebankify.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByFromAccountId(Long fromAccountId, Pageable pageable);
    Page<Transaction> findByToAccountId(Long toAccountId, Pageable pageable);
    Page<Transaction> findAll(Pageable pageable);
}