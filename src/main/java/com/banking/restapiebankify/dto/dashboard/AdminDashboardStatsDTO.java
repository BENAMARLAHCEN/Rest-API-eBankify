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
public class AdminDashboardStatsDTO {
    private UserStatsDTO userStats;
    private TransactionStatsDTO transactionStats;
    private LoanStatsDTO loanStats;
    private AccountStatsDTO accountStats;
    private List<ActivityDTO> recentActivity;
    private SystemHealthDTO systemHealth;
    private List<AlertDTO> alerts;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserStatsDTO {
        private long totalUsers;
        private long newUsers;
        private long activeUsers;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TransactionStatsDTO {
        private long totalTransactions;
        private long pendingTransactions;
        private BigDecimal totalAmount;
        private long monthlyTransactionCount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoanStatsDTO {
        private long totalLoans;
        private long activeLoans;
        private long pendingLoans;
        private BigDecimal totalLoanAmount;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AccountStatsDTO {
        private long totalAccounts;
        private long activeAccounts;
        private long blockedAccounts;
        private BigDecimal totalBalance;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ActivityDTO {
        private Long id;
        private String type;
        private String description;
        private LocalDateTime timestamp;
        private String status;
        private Long userId;
        private String username;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SystemHealthDTO {
        private String status;
        private long uptime;
        private LocalDateTime lastChecked;
        private long activeConnections;
        private double cpuUsage;
        private double memoryUsage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AlertDTO {
        private Long id;
        private String severity;
        private String title;
        private String message;
        private LocalDateTime timestamp;
        private boolean isRead;
    }
}