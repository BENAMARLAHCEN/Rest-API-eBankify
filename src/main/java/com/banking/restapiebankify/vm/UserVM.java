package com.banking.restapiebankify.vm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserVM {
    private Long id;
    private String username;
    private String email;
    private LocalDate customerSince;
    private Double age;
    private BigDecimal monthlyIncome;
    private String collateralAvailable; // Description of collateral
}
