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
    private String type;
    private String status;
    private Long fromAccountId;
    private Long toAccountId;
    private LocalDateTime timestamp;
    private String frequency;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
