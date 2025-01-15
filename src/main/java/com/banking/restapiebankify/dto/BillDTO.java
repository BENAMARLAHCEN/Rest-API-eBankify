package com.banking.restapiebankify.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BillDTO {
    private Long id;
    private Long userId; // Linking to the user to whom the bill is associated
    private String biller; // The entity requesting payment
    private BigDecimal amount;
    private LocalDate dueDate;
    private String status; // UNPAID, PAID, OVERDUE
}
