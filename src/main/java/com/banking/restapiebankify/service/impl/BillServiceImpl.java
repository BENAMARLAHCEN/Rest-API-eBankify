package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.mapper.BillMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.BillStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.BillRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.BillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    @Autowired
    public BillServiceImpl(BillRepository billRepository, UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Bill createBill(BillDTO billDTO) {
        Bill bill = BillMapper.INSTANCE.toBill(billDTO);
        bill.setStatus(BillStatus.UNPAID);
        bill.setDueDate(LocalDateTime.now().plusMonths(1));
        return billRepository.save(bill);
    }

    @Override
    public List<Bill> getBillsByUser(Long userId) {
        return billRepository.findByUserId(userId);
    }

    @Override
    public Bill payBill(Long billId, Long userId, String accountNumber) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new RuntimeException("Bill not found"));
        BankAccount bankAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Bank account not found"));
        if (!bill.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to pay this bill");
        }
        if (!bankAccount.getUser().getId().equals(userId)) {
            throw new RuntimeException("You are not authorized to pay from this account");
        }
        if (bill.getStatus().equals(BillStatus.PAID)) {
            throw new RuntimeException("Bill is already paid");
        }
        if (bankAccount.getBalance().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        bill.setStatus(BillStatus.PAID);
        bankAccount.setBalance(bankAccount.getBalance().subtract(bill.getAmount()));
        bankAccountRepository.save(bankAccount);
        return billRepository.save(bill);
    }
}