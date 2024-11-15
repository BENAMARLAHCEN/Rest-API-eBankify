package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.BillDTO;
import com.banking.restapiebankify.model.Bill;
import com.banking.restapiebankify.model.User;
import com.banking.restapiebankify.service.BillService;
import com.banking.restapiebankify.service.UserService;
import com.banking.restapiebankify.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bills")
public class BillController {

    private final BillService billService;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @Autowired
    public BillController(BillService billService, UserService userService, JwtUtil jwtUtil) {
        this.billService = billService;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    private User getUserFromToken(String token) {
        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        String username = jwtUtil.extractUsername(token);
        return userService.findUserByUsername(username);
    }

    @PostMapping("/create")
    public ResponseEntity<Bill> createBill(@RequestHeader("Authorization") String token, @RequestBody BillDTO billDTO) {
        User user = getUserFromToken(token);
        if (user == null || user.getRole().getName().equals("USER")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Bill createdBill = billService.createBill(billDTO);
        return new ResponseEntity<>(createdBill, HttpStatus.CREATED);
    }

    @GetMapping("/myBill")
    public ResponseEntity<List<Bill>> getBillsForUser(@RequestHeader("Authorization") String token) {
        User user = getUserFromToken(token);
        if (user == null || user.getRole().getName().equals("ADMIN") || user.getRole().getName().equals("EMPLOYEE")) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        List<Bill> bills = billService.getBillsByUser(user.getId());
        return new ResponseEntity<>(bills, HttpStatus.OK);
    }

    @PatchMapping("/pay/{billId}")
    public ResponseEntity<Bill> payBill(@RequestHeader("Authorization") String token, @PathVariable Long billId, @RequestParam String acountNumber) {
        User user = getUserFromToken(token);
        Bill paidBill = billService.payBill(billId, user.getId(), acountNumber);
        return new ResponseEntity<>(paidBill, HttpStatus.OK);
    }
}