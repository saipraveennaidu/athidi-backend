package com.athidi.admin.service;

import com.athidi.admin.dto.AdminBookingResponse;
import com.athidi.admin.dto.AdminDashboardResponse;
import com.athidi.admin.dto.AdminUserResponse;
import com.athidi.booking.entity.Booking;
import com.athidi.booking.repository.BookingRepository;
import com.athidi.common.enums.BookingStatus;
import com.athidi.common.enums.Role;
import com.athidi.common.validation.PageRequestValidator;
import com.athidi.common.validation.SortFieldValidator;
import com.athidi.exception.ResourceNotFoundException;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final PageRequestValidator pageRequestValidator;
    private final SortFieldValidator sortFieldValidator;

    @Override
    public Page<AdminUserResponse> getAllUsers(
            int page,
            int size,
            String sortBy) {

        pageRequestValidator.validate(page, size);

        sortFieldValidator.validate(
                sortBy,
                Set.of(
                        "id",
                        "createdAt",
                        "email",
                        "firstName"
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).descending()
        );

        return userRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    @Override
    public void addRoleToUser(Long userId, Role role) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        user.getRoles().add(role);

        userRepository.save(user);
    }

    @Transactional
    @Override
    public void updateUserStatus(Long userId, boolean active) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"));

        user.setActive(active);

        userRepository.save(user);
    }

    @Override
    public Page<AdminBookingResponse> getAllBookings(
            int page,
            int size) {

        pageRequestValidator.validate(page, size);

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("createdAt").descending()
        );

        return bookingRepository
                .findAllByOrderByCreatedAtDesc(pageable)
                .map(this::mapBookingToResponse);
    }

    private AdminUserResponse mapToResponse(User user) {

        return AdminUserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .roles(user.getRoles())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

    private AdminBookingResponse mapBookingToResponse(
            Booking booking) {

        return AdminBookingResponse.builder()
                .id(booking.getId())
                .propertyId(booking.getProperty().getId())
                .propertyTitle(booking.getProperty().getTitle())
                .customerId(booking.getCustomer().getId())
                .customerName(
                        booking.getCustomer().getFirstName()
                                + " "
                                + booking.getCustomer().getLastName()
                )
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numberOfGuests(booking.getNumberOfGuests())
                .pricePerNight(booking.getPricePerNight())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }

    @Override
    public AdminDashboardResponse getDashboard() {

        long totalUsers = userRepository.count();

        long totalCustomers =
                userRepository.countByRolesContaining(Role.CUSTOMER);

        long totalOwners =
                userRepository.countByRolesContaining(Role.OWNER);

        long totalAdmins =
                userRepository.countByRolesContaining(Role.ADMIN);

        long totalProperties =
                propertyRepository.count();

        long activeProperties =
                propertyRepository.countByActiveTrue();

        long totalBookings =
                bookingRepository.count();

        long pendingBookings =
                bookingRepository.countByStatus(BookingStatus.PENDING);

        long confirmedBookings =
                bookingRepository.countByStatus(BookingStatus.CONFIRMED);

        long completedBookings =
                bookingRepository.countByStatus(BookingStatus.COMPLETED);

        BigDecimal totalRevenue =
                bookingRepository.getTotalRevenueByStatus(
                        BookingStatus.COMPLETED
                );

        return AdminDashboardResponse.builder()
                .totalUsers(totalUsers)
                .totalCustomers(totalCustomers)
                .totalOwners(totalOwners)
                .totalAdmins(totalAdmins)
                .totalProperties(totalProperties)
                .activeProperties(activeProperties)
                .totalBookings(totalBookings)
                .pendingBookings(pendingBookings)
                .confirmedBookings(confirmedBookings)
                .completedBookings(completedBookings)
                .totalRevenue(totalRevenue)
                .build();
    }
}
