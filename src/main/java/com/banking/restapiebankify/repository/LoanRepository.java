package com.banking.restapiebankify.repository;

import com.banking.restapiebankify.model.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loan, Long> {
    Page<Loan> findByUserId(Long userId, Pageable pageable);
    Page<Loan> findAll(Pageable pageable);
}
