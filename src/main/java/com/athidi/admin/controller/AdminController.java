package com.athidi.admin.controller;

import com.athidi.admin.dto.AdminBookingResponse;
import com.athidi.admin.dto.AdminDashboardResponse;
import com.athidi.admin.dto.AdminUserResponse;
import com.athidi.admin.dto.UpdateRoleRequest;
import com.athidi.admin.service.AdminService;
import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import com.athidi.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
public class AdminController {
    private final AdminService adminService;
    private final ApiResponseBuilder responseBuilder;
    private final PropertyService propertyService;

    @GetMapping("/users")
    public ResponseEntity<ApiResponse<Page<AdminUserResponse>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Page<AdminUserResponse> response =
                adminService.getAllUsers(
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Users fetched successfully",
                        response
                )
        );
    }

    @PatchMapping("/users/{userId}/roles")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<String>> addRoleToUser(
            @PathVariable Long userId,
            @Valid @RequestBody UpdateRoleRequest request) {

        adminService.addRoleToUser(
                userId,
                request.getRole()
        );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Role added successfully",
                        "Role " + request.getRole() +
                                " added to user"
                )
        );
    }

    @PatchMapping("/users/{userId}/status")
    public ResponseEntity<ApiResponse<String>> updateUserStatus(
            @PathVariable Long userId,
            @RequestParam boolean active) {

        adminService.updateUserStatus(userId, active);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "User status updated successfully",
                        "User active status: " + active
                )
        );
    }

    @PatchMapping("/properties/{propertyId}/status")
    public ResponseEntity<ApiResponse<String>> updatePropertyStatus(
            @PathVariable Long propertyId,
            @RequestParam boolean active) {

        propertyService.updatePropertyStatus(propertyId, active);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Property status updated successfully",
                        "Property active status: " + active
                )
        );
    }

    @GetMapping("/bookings")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<Page<AdminBookingResponse>>> getAllBookings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<AdminBookingResponse> response =
                adminService.getAllBookings(page, size);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Bookings fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<ApiResponse<AdminDashboardResponse>> getDashboard() {

        AdminDashboardResponse response =
                adminService.getDashboard();

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Dashboard data fetched successfully",
                        response
                )
        );
    }
}
