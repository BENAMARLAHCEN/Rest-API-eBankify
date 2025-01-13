
package com.banking.restapiebankify.service;

import com.banking.restapiebankify.dto.dashboard.AdminDashboardStatsDTO;
import com.banking.restapiebankify.dto.dashboard.UserDashboardStatsDTO;
import com.banking.restapiebankify.model.*;
import com.banking.restapiebankify.model.enums.*;
import com.banking.restapiebankify.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final LoanRepository loanRepository;
    private final BillRepository billRepository;
    private final BankAccountRepository bankAccountRepository;

    public AdminDashboardStatsDTO getAdminDashboardStats() {
        return AdminDashboardStatsDTO.builder()
                .userStats(getUserStats())
                .transactionStats(getTransactionStats())
                .loanStats(getLoanStats())
                .accountStats(getAccountStats())
                .recentActivity(getRecentActivity())
                .systemHealth(getSystemHealth())
                .alerts(getUnreadAlerts())
                .build();
    }

    public UserDashboardStatsDTO getUserDashboardStats() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserDashboardStatsDTO.builder()
                .accountSummary(getUserAccountSummary(user))
                .recentTransactions(getUserRecentTransactions(user))
                .loanSummary(getUserLoanSummary(user))
                .billsSummary(getUserBillsSummary(user))
                .build();
    }

    private AdminDashboardStatsDTO.UserStatsDTO getUserStats() {
        long totalUsers = userRepository.count();
        // Additional logic for new and active users
        return AdminDashboardStatsDTO.UserStatsDTO.builder()
                .totalUsers(totalUsers)
                .newUsers(0) // Implement logic
                .activeUsers(0) // Implement logic
                .build();
    }

    private AdminDashboardStatsDTO.TransactionStatsDTO getTransactionStats() {
        return AdminDashboardStatsDTO.TransactionStatsDTO.builder()
                .totalTransactions(transactionRepository.count())
                .pendingTransactions(0) // Implement count of PENDING transactions
                .totalAmount(BigDecimal.ZERO) // Implement sum
                .monthlyTransactionCount(0) // Implement monthly count
                .build();
    }

    private AdminDashboardStatsDTO.LoanStatsDTO getLoanStats() {
        return AdminDashboardStatsDTO.LoanStatsDTO.builder()
                .totalLoans(loanRepository.count())
                .activeLoans(0) // Implement count of active loans
                .pendingLoans(0) // Implement count of pending loans
                .totalLoanAmount(BigDecimal.ZERO) // Implement sum
                .build();
    }

    private AdminDashboardStatsDTO.AccountStatsDTO getAccountStats() {
        return AdminDashboardStatsDTO.AccountStatsDTO.builder()
                .totalAccounts(bankAccountRepository.count())
                .activeAccounts(0) // Implement count of active accounts
                .blockedAccounts(0) // Implement count of blocked accounts
                .totalBalance(BigDecimal.ZERO) // Implement sum
                .build();
    }

    private List<AdminDashboardStatsDTO.ActivityDTO> getRecentActivity() {
        // Implement recent activity logic
        return List.of();
    }

    public AdminDashboardStatsDTO.SystemHealthDTO getSystemHealth() {
        return AdminDashboardStatsDTO.SystemHealthDTO.builder()
                .status("HEALTHY")
                .uptime(0)
                .lastChecked(LocalDateTime.now())
                .activeConnections(0)
                .cpuUsage(0.0)
                .memoryUsage(0.0)
                .build();
    }

    public List<AdminDashboardStatsDTO.AlertDTO> getUnreadAlerts() {
        // Implement alerts logic
        return List.of();
    }

    public void markAlertAsRead(Long alertId) {
        // Implement mark alert as read logic
    }

    private UserDashboardStatsDTO.AccountSummaryDTO getUserAccountSummary(User user) {
        List<BankAccount> accounts = bankAccountRepository.findByUser(user);
        BigDecimal totalBalance = accounts.stream()
                .map(BankAccount::getBalance)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return UserDashboardStatsDTO.AccountSummaryDTO.builder()
                .totalBalance(totalBalance)
                .totalAccounts(accounts.size())
                .monthlyIncome(user.getMonthlyIncome())
                .monthlyExpenses(BigDecimal.ZERO) // Implement calculation
                .build();
    }

    private List<UserDashboardStatsDTO.RecentTransactionDTO> getUserRecentTransactions(User user) {
        List<BankAccount> userAccounts = bankAccountRepository.findByUser(user);
        return userAccounts.stream()
                .flatMap(account -> transactionRepository.findByFromAccountId(account.getId(), null).stream())
                .map(this::mapToRecentTransactionDTO)
                .collect(Collectors.toList());
    }

    private UserDashboardStatsDTO.RecentTransactionDTO mapToRecentTransactionDTO(Transaction transaction) {
        return UserDashboardStatsDTO.RecentTransactionDTO.builder()
                .id(transaction.getId())
                .type(transaction.getType().toString())
                .amount(transaction.getAmount())
                .description("Transaction " + transaction.getId())
                .date(transaction.getTimestamp())
                .status(transaction.getStatus().toString())
                .build();
    }

    private UserDashboardStatsDTO.LoanSummaryDTO getUserLoanSummary(User user) {
        // Implement loan summary logic
        return UserDashboardStatsDTO.LoanSummaryDTO.builder()
                .activeLoans(0)
                .totalLoanAmount(BigDecimal.ZERO)
                .build();
    }

    private UserDashboardStatsDTO.BillsSummaryDTO getUserBillsSummary(User user) {
        // Implement bills summary logic
        return UserDashboardStatsDTO.BillsSummaryDTO.builder()
                .pendingBills(0)
                .totalDueAmount(BigDecimal.ZERO)
                .build();
    }
}