package com.banking.restapiebankify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDTO {
    private Long id;
    private BigDecimal amount;
    private String type; // CLASSIC, INSTANT, STANDING_ORDER
    private String status; // PENDING, COMPLETED, REJECTED
    private Long fromAccountId; // ID of the account initiating the transaction
    private Long toAccountId; // ID of the account receiving the transaction
    private LocalDateTime timestamp;
    private String frequency; // DAILY, WEEKLY, MONTHLY, etc., for STANDING_ORDER transactions
    private LocalDateTime startDate; // Start date for STANDING_ORDER transactions
    private LocalDateTime endDate; // End date for STANDING_ORDER transactions
}
