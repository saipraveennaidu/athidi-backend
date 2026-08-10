package com.athidi.admin.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record AdminDashboardResponse(
        long totalUsers,

        long totalCustomers,

        long totalOwners,

        long totalAdmins,

        long totalProperties,

        long activeProperties,

        long totalBookings,

        long pendingBookings,

        long confirmedBookings,

        long completedBookings,

        BigDecimal totalRevenue

) {
}
