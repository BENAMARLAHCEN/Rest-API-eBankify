package com.banking.restapiebankify.repository;

import com.banking.restapiebankify.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BillRepository extends JpaRepository<Bill, Long> {
    Page<Bill> findByUserId(Long userId, Pageable pageable);
    Page<Bill> findAll(Pageable pageable);
}