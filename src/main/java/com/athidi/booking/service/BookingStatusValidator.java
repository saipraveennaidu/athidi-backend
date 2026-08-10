package com.athidi.booking.service;

import com.athidi.common.enums.BookingStatus;
import com.athidi.exception.BookingException;
import org.springframework.stereotype.Component;

@Component
public class BookingStatusValidator {
    public void validateConfirmation(BookingStatus status) {

        if (status != BookingStatus.PENDING) {
            throw new BookingException(
                    "Only pending bookings can be confirmed"
            );
        }
    }

    public void validateCompletion(BookingStatus status) {

        if (status != BookingStatus.CONFIRMED) {
            throw new BookingException(
                    "Only confirmed bookings can be completed"
            );
        }
    }

    public void validateCancellation(BookingStatus status) {

        if (status == BookingStatus.CANCELLED) {
            throw new BookingException(
                    "Booking is already cancelled"
            );
        }

        if (status == BookingStatus.COMPLETED) {
            throw new BookingException(
                    "Completed booking cannot be cancelled"
            );
        }
    }
}
