package com.banking.restapiebankify.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountVM {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String status; // ACTIVE or BLOCKED
}
