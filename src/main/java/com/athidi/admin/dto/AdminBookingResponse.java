package com.athidi.admin.dto;

import com.athidi.common.enums.BookingStatus;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record AdminBookingResponse(
        Long id,

        Long propertyId,
        String propertyTitle,

        Long customerId,
        String customerName,

        LocalDate checkInDate,
        LocalDate checkOutDate,

        Integer numberOfGuests,

        BigDecimal pricePerNight,
        BigDecimal totalPrice,

        BookingStatus status
) {
}
