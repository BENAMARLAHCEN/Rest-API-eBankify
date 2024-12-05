package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.mapper.BillMapper;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.service.BillService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;

    public BillController(BillService billService) {
        this.billService = billService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<BillDTO> createBill(@RequestBody BillDTO billDTO) {
        Bill bill = billService.createBill(billDTO);
        return ResponseEntity.status(201).body(BillMapper.INSTANCE.toBillDTO(bill));
    }

    @GetMapping("/myBill")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<BillDTO>> getBillsForUser() {
        List<Bill> bills = billService.getBillsByUser(getCurrentUserId());
        List<BillDTO> billDTOs = bills.stream()
                .map(BillMapper.INSTANCE::toBillDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(billDTOs);
    }

    @PatchMapping("/pay/{billId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BillDTO> payBill(@PathVariable Long billId, @RequestParam String accountNumber) {
        Bill paidBill = billService.payBill(billId, getCurrentUserId(), accountNumber);
        return ResponseEntity.ok(BillMapper.INSTANCE.toBillDTO(paidBill));
    }

    private Long getCurrentUserId() {
        String username = org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
        return Long.parseLong(username); // Adjust logic if `username` isn't directly the ID
    }
}
