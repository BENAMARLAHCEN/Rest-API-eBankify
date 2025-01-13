package com.banking.restapiebankify.dto.dashboard;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDashboardStatsDTO {
    private AccountSummaryDTO accountSummary;
    private List<RecentTransactionDTO> recentTransactions;
    private LoanSummaryDTO loanSummary;
    private BillsSummaryDTO billsSummary;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountSummaryDTO {
        private BigDecimal totalBalance;
        private long totalAccounts;
        private BigDecimal monthlyIncome;
        private BigDecimal monthlyExpenses;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecentTransactionDTO {
        private Long id;
        private String type;
        private BigDecimal amount;
        private String description;
        private LocalDateTime date;
        private String status;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanSummaryDTO {
        private long activeLoans;
        private BigDecimal totalLoanAmount;
        private LocalDateTime nextPaymentDate;
        private BigDecimal nextPaymentAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BillsSummaryDTO {
        private long pendingBills;
        private BigDecimal totalDueAmount;
        private LocalDateTime nextDueDate;
        private BigDecimal nextDueAmount;
    }
}
