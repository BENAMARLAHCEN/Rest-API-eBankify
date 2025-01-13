package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.mapper.BillMapper;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.service.BillService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/all")
    @PreAuthorize("hasAnyRole('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<Page<BillDTO>> getAllBills(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Bill> bills = billService.getAllBills(pageable);
        Page<BillDTO> billDTOs = bills.map(BillMapper.INSTANCE::toBillDTO);
        return ResponseEntity.ok(billDTOs);
    }

    @GetMapping("/myBill")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Page<BillDTO>> getBillsForUser(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "dueDate") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction.toUpperCase());
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<Bill> bills = billService.getBillsByUser(getCurrentUserUsername(), pageable);
        Page<BillDTO> billDTOs = bills.map(BillMapper.INSTANCE::toBillDTO);
        return ResponseEntity.ok(billDTOs);
    }

    @PatchMapping("/pay/{billId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<BillDTO> payBill(@PathVariable Long billId, @RequestParam String accountNumber) {
        Bill paidBill = billService.payBill(billId, getCurrentUserUsername(), accountNumber);
        return ResponseEntity.ok(BillMapper.INSTANCE.toBillDTO(paidBill));
    }

    private String getCurrentUserUsername() {
        return org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication().getName();
    }
}