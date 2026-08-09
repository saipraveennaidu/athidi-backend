package com.athidi.booking.controller;

import com.athidi.booking.dto.BookingResponse;
import com.athidi.booking.dto.CreateBookingRequest;
import com.athidi.booking.service.BookingService;
import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;
    private final ApiResponseBuilder responseBuilder;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<BookingResponse>> createBooking(
            @Valid @RequestBody CreateBookingRequest request) {

        BookingResponse response =
                bookingService.createBooking(request);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Booking created successfully",
                        response
                )
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getMyBookings(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "checkInDate") String sortBy) {

        Page<BookingResponse> response =
                bookingService.getMyBookings(
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Bookings fetched successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> cancelBooking(
            @PathVariable Long id) {

        bookingService.cancelBooking(id);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Booking cancelled successfully",
                        "Cancelled"
                )
        );
    }

    @GetMapping("/owner")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<Page<BookingResponse>>> getOwnerBookings(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "checkInDate") String sortBy) {

        Page<BookingResponse> response =
                bookingService.getOwnerBookings(
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Owner bookings fetched successfully",
                        response
                )
        );
    }

    @PatchMapping("/{id}/confirm")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> confirmBooking(
            @PathVariable Long id) {

        bookingService.confirmBooking(id);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Booking confirmed successfully",
                        "Confirmed"
                )
        );
    }
}
