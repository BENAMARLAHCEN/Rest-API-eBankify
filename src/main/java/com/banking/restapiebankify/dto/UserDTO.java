package com.banking.restapiebankify.dto;

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
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private LocalDate birthday;
    private BigDecimal monthlyIncome;
    private String collateralAvailable;
    private LocalDate customerSince;
    private String role; // For simplicity, using String to represent role name
}
