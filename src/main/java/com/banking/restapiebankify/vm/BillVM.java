package com.banking.restapiebankify.vm;

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
public class BillVM {
    private Long id;
    private String biller; // The entity requesting payment
    private BigDecimal amount;
    private LocalDateTime dueDate;
    private String status; // UNPAID, PAID, OVERDUE
}
