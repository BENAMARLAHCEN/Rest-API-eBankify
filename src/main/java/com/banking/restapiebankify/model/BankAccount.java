package com.banking.restapiebankify.model;

import com.banking.restapiebankify.model.enums.AccountStatus;
import com.banking.restapiebankify.model.enums.AccountType;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String accountNumber;

    @Column(nullable = false)
    private BigDecimal balance;

    @JsonSerialize
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    private AccountStatus status; // ACTIVE or BLOCKED

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "varchar(255) default 'SAVINGS'")
    private AccountType type; // SAVINGS or CHECKING
}
