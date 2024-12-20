package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.exception.UserNotFoundException;
import com.banking.restapiebankify.mapper.BillMapper;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.BillStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.BillRepository;
import com.banking.restapiebankify.repository.UserRepository;
import com.banking.restapiebankify.service.BillService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BillServiceImpl implements BillService {

    private final BillRepository billRepository;
    private final UserRepository userRepository;
    private final BankAccountRepository bankAccountRepository;

    public BillServiceImpl(BillRepository billRepository, UserRepository userRepository, BankAccountRepository bankAccountRepository) {
        this.billRepository = billRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public Bill createBill(BillDTO billDTO) {
        Bill bill = BillMapper.INSTANCE.toBill(billDTO);
        User user = userRepository.findById(billDTO.getUserId())
                .orElseThrow(() -> new UserNotFoundException("User not found"));
        bill.setUser(user);
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
            throw new RuntimeException("Unauthorized access to this bill");
        }
        if (!bankAccount.getUser().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized access to this bank account");
        }
        if (bill.getStatus().equals(BillStatus.PAID)) {
            throw new RuntimeException("Bill is already paid");
        }
        if (bankAccount.getBalance().compareTo(bill.getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds");
        }
        bankAccount.setBalance(bankAccount.getBalance().subtract(bill.getAmount()));
        bill.setStatus(BillStatus.PAID);
        bankAccountRepository.save(bankAccount);
        return billRepository.save(bill);
    }
}