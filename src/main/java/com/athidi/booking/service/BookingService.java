package com.athidi.booking.service;

import com.athidi.booking.dto.BookingResponse;
import com.athidi.booking.dto.CreateBookingRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BookingService {
    BookingResponse createBooking(CreateBookingRequest request);

    Page<BookingResponse> getMyBookings(
            int page,
            int size,
            String sortBy
    );

    void cancelBooking(Long bookingId);

    Page<BookingResponse> getOwnerBookings(
            int page,
            int size,
            String sortBy
    );

    void confirmBooking(Long bookingId);
}
