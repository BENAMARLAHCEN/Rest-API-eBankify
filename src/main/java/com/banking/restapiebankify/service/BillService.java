package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.Bill;

import java.util.List;

public interface BillService {
    Bill createBill(BillDTO billDTO);
    List<Bill> getBillsByUser(Long userId);
    Bill payBill(Long billId, Long userId, String accountNumber);
}
