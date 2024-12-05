package com.banking.restapiebankify.repository;

import com.banking.restapiebankify.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByIdAndUserId(Long id, Long userId);

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByUserId(Long id);
}
