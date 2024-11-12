package com.banking.restapiebankify.model;

import com.banking.restapiebankify.model.enums.TransactionStatus;
import com.banking.restapiebankify.model.enums.TransactionType;
import lombok.*;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "from_account_id")
    private BankAccount fromAccount;

    @ManyToOne
    @JoinColumn(name = "to_account_id")
    private BankAccount toAccount;

    @Column(nullable = false)
    private LocalDateTime timestamp;

    // Fields for Standing Order
    private String frequency; // DAILY, WEEKLY, MONTHLY, etc.
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
