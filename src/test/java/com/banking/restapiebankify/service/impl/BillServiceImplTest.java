package com.banking.restapiebankify.service.impl;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.BankAccount;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.model.enums.BillStatus;
import com.banking.restapiebankify.repository.BankAccountRepository;
import com.banking.restapiebankify.repository.BillRepository;
import com.banking.restapiebankify.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BillServiceImplTest {

    @Mock
    private BillRepository billRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BillServiceImpl billService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createBill_ShouldReturnCreatedBill() {
        User mockUser = new User();
        mockUser.setId(1L);

        BillDTO billDTO = new BillDTO();
        billDTO.setUserId(1L);
        billDTO.setBiller("Electricity");
        billDTO.setAmount(BigDecimal.valueOf(100));

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(billRepository.save(any(Bill.class))).thenAnswer(invocation -> {
            Bill savedBill = invocation.getArgument(0);
            savedBill.setId(1L);
            return savedBill;
        });

        Bill createdBill = billService.createBill(billDTO);

        assertNotNull(createdBill);
        assertEquals(1L, createdBill.getId());
        assertEquals(BillStatus.UNPAID, createdBill.getStatus());
        verify(userRepository, times(1)).findById(1L);
        verify(billRepository, times(1)).save(any(Bill.class));
    }

    @Test
    void getBillsByUser_ShouldReturnListOfBills() {
        Bill bill1 = new Bill();
        bill1.setId(1L);
        Bill bill2 = new Bill();
        bill2.setId(2L);

        when(billRepository.findByUserId(1L)).thenReturn(List.of(bill1, bill2));

        List<Bill> bills = billService.getBillsByUser(1L);

        assertNotNull(bills);
        assertEquals(2, bills.size());
        verify(billRepository, times(1)).findByUserId(1L);
    }

    @Test
    void payBill_ShouldUpdateBillStatusToPaid() {
        User mockUser = new User();
        mockUser.setId(1L);

        BankAccount mockAccount = new BankAccount();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber("123456");
        mockAccount.setBalance(BigDecimal.valueOf(200));
        mockAccount.setUser(mockUser);

        Bill unpaidBill = new Bill();
        unpaidBill.setId(1L);
        unpaidBill.setUser(mockUser);
        unpaidBill.setAmount(BigDecimal.valueOf(100));
        unpaidBill.setStatus(BillStatus.UNPAID);

        when(billRepository.findById(1L)).thenReturn(Optional.of(unpaidBill));
        when(bankAccountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(mockAccount));
        when(billRepository.save(any(Bill.class))).thenReturn(unpaidBill);
        when(bankAccountRepository.save(any(BankAccount.class))).thenReturn(mockAccount);

        Bill paidBill = billService.payBill(1L, 1L, "123456");

        assertNotNull(paidBill);
        assertEquals(BillStatus.PAID, paidBill.getStatus());
        assertEquals(BigDecimal.valueOf(100), mockAccount.getBalance());
        verify(billRepository, times(1)).save(unpaidBill);
        verify(bankAccountRepository, times(1)).save(mockAccount);
    }

    @Test
    void payBill_ShouldThrowException_WhenInsufficientFunds() {
        User mockUser = new User();
        mockUser.setId(1L);

        BankAccount mockAccount = new BankAccount();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber("123456");
        mockAccount.setBalance(BigDecimal.valueOf(50));
        mockAccount.setUser(mockUser);

        Bill unpaidBill = new Bill();
        unpaidBill.setId(1L);
        unpaidBill.setUser(mockUser);
        unpaidBill.setAmount(BigDecimal.valueOf(100));
        unpaidBill.setStatus(BillStatus.UNPAID);

        when(billRepository.findById(1L)).thenReturn(Optional.of(unpaidBill));
        when(bankAccountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(mockAccount));

        assertThrows(RuntimeException.class, () -> billService.payBill(1L, 1L, "123456"));
        verify(billRepository, never()).save(any(Bill.class));
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }

    @Test
    void payBill_ShouldThrowException_WhenUnauthorizedUser() {
        User mockUser = new User();
        mockUser.setId(1L);

        User otherUser = new User();
        otherUser.setId(2L);

        BankAccount mockAccount = new BankAccount();
        mockAccount.setId(1L);
        mockAccount.setAccountNumber("123456");
        mockAccount.setBalance(BigDecimal.valueOf(200));
        mockAccount.setUser(otherUser);

        Bill unpaidBill = new Bill();
        unpaidBill.setId(1L);
        unpaidBill.setUser(mockUser);
        unpaidBill.setAmount(BigDecimal.valueOf(100));
        unpaidBill.setStatus(BillStatus.UNPAID);

        when(billRepository.findById(1L)).thenReturn(Optional.of(unpaidBill));
        when(bankAccountRepository.findByAccountNumber("123456")).thenReturn(Optional.of(mockAccount));

        assertThrows(RuntimeException.class, () -> billService.payBill(1L, 1L, "123456"));
        verify(billRepository, never()).save(any(Bill.class));
        verify(bankAccountRepository, never()).save(any(BankAccount.class));
    }
}
