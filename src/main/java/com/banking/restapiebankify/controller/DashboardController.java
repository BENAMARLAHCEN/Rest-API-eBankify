package com.banking.restapiebankify.controller;

import com.banking.restapiebankify.dto.dashboard.AdminDashboardStatsDTO;
import com.banking.restapiebankify.dto.dashboard.UserDashboardStatsDTO;
import com.banking.restapiebankify.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/admin/stats")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardStatsDTO> getAdminStats() {
        return ResponseEntity.ok(dashboardService.getAdminDashboardStats());
    }

    @GetMapping("/user/stats")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<UserDashboardStatsDTO> getUserStats() {
        return ResponseEntity.ok(dashboardService.getUserDashboardStats());
    }

    @GetMapping("/admin/system-health")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardStatsDTO.SystemHealthDTO> getSystemHealth() {
        return ResponseEntity.ok(dashboardService.getSystemHealth());
    }

    @GetMapping("/admin/alerts")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<java.util.List<AdminDashboardStatsDTO.AlertDTO>> getUnreadAlerts() {
        return ResponseEntity.ok(dashboardService.getUnreadAlerts());
    }

    @PatchMapping("/admin/alerts/{alertId}/mark-read")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> markAlertAsRead(@PathVariable Long alertId) {
        dashboardService.markAlertAsRead(alertId);
        return ResponseEntity.ok().build();
    }
}