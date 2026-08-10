package com.athidi.admin.service;

import com.athidi.admin.dto.AdminBookingResponse;
import com.athidi.admin.dto.AdminDashboardResponse;
import com.athidi.admin.dto.AdminUserResponse;
import com.athidi.common.enums.Role;
import org.springframework.data.domain.Page;

public interface AdminService {
    Page<AdminUserResponse> getAllUsers(
            int page,
            int size,
            String sortBy
    );

    void addRoleToUser(Long userId, Role role);

    void updateUserStatus(Long userId, boolean active);

    Page<AdminBookingResponse> getAllBookings(
            int page,
            int size
    );

    AdminDashboardResponse getDashboard();
}
