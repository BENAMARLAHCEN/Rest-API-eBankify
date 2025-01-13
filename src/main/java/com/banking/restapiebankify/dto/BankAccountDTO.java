package com.banking.restapiebankify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountDTO {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String type; // SAVINGS or CHECKING
    private String status; // ACTIVE or BLOCKED
    private Long userId; // Linking to the user who owns the account
}
