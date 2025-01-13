package com.banking.restapiebankify.repository;

import com.banking.restapiebankify.model.BankAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
    Optional<BankAccount> findByIdAndUserId(Long id, Long userId);
    Optional<BankAccount> findByAccountNumber(String accountNumber);
    Page<BankAccount> findByUserId(Long id, Pageable pageable);
    Page<BankAccount> findAll(Pageable pageable);
}
