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
public class LoanDTO {
    private Long id;
    private Long userId; // Linking to the user who took the loan
    private BigDecimal amount;
    private BigDecimal interestRate;
    private LocalDateTime loanStartDate;
    private LocalDateTime loanEndDate;
    private String status; // PENDING, APPROVED, REJECTED, ONGOING, COMPLETED
}
