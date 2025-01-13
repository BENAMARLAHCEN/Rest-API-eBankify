package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.Bill;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BillService {
    Bill createBill(BillDTO billDTO);
    Page<Bill> getBillsByUser(String username, Pageable pageable);
    Bill payBill(Long billId, String username, String accountNumber);
    Page<Bill> getAllBills(Pageable pageable);
}