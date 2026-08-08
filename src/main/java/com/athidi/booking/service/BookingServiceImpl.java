package com.athidi.booking.service;

import com.athidi.booking.dto.BookingResponse;
import com.athidi.booking.dto.CreateBookingRequest;
import com.athidi.booking.entity.Booking;
import com.athidi.exception.BookingException;
import com.athidi.booking.repository.BookingRepository;
import com.athidi.common.enums.BookingStatus;
import com.athidi.exception.ResourceNotFoundException;
import com.athidi.property.entity.Property;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.security.SecurityUtils;
import com.athidi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements  BookingService{
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final SecurityUtils securityUtils;

    @Override
    public BookingResponse createBooking(CreateBookingRequest request) {

        // 1. Get currently logged-in customer
        User customer = securityUtils.getCurrentUser();

        // 2. Find property
        Property property = propertyRepository
                .findById(request.getPropertyId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found"));

        // 3. Make sure property is active
        if (!property.getActive()) {
            throw new ResourceNotFoundException(
                    "Property is not available");
        }

        // 4. Validate date range
        if (!request.getCheckOutDate()
                .isAfter(request.getCheckInDate())) {

            throw new BookingException(
                    "Check-out date must be after check-in date");
        }

        // 5. Check guest capacity
        if (request.getNumberOfGuests() > property.getMaxGuests()) {

            throw new BookingException(
                    "Number of guests exceeds property capacity");
        }

        // 6. Check overlapping confirmed booking
        boolean alreadyBooked =
                bookingRepository
                        .existsByPropertyAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
                                property,
                                BookingStatus.CONFIRMED,
                                request.getCheckOutDate(),
                                request.getCheckInDate()
                        );

        if (alreadyBooked) {
            throw new BookingException(
                    "Property is already booked for the selected dates");
        }

        // 7. Calculate number of nights
        long numberOfNights =
                ChronoUnit.DAYS.between(
                        request.getCheckInDate(),
                        request.getCheckOutDate()
                );

        // 8. Calculate total price
        BigDecimal totalPrice =
                property.getPricePerNight()
                        .multiply(BigDecimal.valueOf(numberOfNights));

        // 9. Create booking
        Booking booking = Booking.builder()
                .customer(customer)
                .property(property)
                .checkInDate(request.getCheckInDate())
                .checkOutDate(request.getCheckOutDate())
                .numberOfGuests(request.getNumberOfGuests())
                .pricePerNight(property.getPricePerNight())
                .totalPrice(totalPrice)
                .status(BookingStatus.PENDING)
                .build();

        // 10. Save
        booking = bookingRepository.save(booking);

        // 11. Return response
        return mapToResponse(booking);
    }

    @Override
    public Page<BookingResponse> getMyBookings(
            int page,
            int size,
            String sortBy) {

        User customer = securityUtils.getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        return bookingRepository
                .findByCustomer(customer, pageable)
                .map(this::mapToResponse);
    }

    private BookingResponse mapToResponse(Booking booking) {

        return BookingResponse.builder()
                .id(booking.getId())
                .propertyId(booking.getProperty().getId())
                .propertyTitle(booking.getProperty().getTitle())
                .customerId(booking.getCustomer().getId())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .numberOfGuests(booking.getNumberOfGuests())
                .pricePerNight(booking.getPricePerNight())
                .totalPrice(booking.getTotalPrice())
                .status(booking.getStatus())
                .build();
    }

    @Override
    public void cancelBooking(Long bookingId) {

        User customer = securityUtils.getCurrentUser();

        Booking booking = bookingRepository
                .findByIdAndCustomer(bookingId, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Booking not found"));

        if (booking.getStatus() == BookingStatus.CANCELLED) {
            throw new BookingException(
                    "Booking is already cancelled");
        }

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BookingException(
                    "Completed booking cannot be cancelled");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }

    @Override
    public Page<BookingResponse> getOwnerBookings(
            int page,
            int size,
            String sortBy) {

        User owner = securityUtils.getCurrentUser();

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        return bookingRepository
                .findByPropertyOwner(owner, pageable)
                .map(this::mapToResponse);
    }

    @Override
    public void confirmBooking(Long bookingId) {

        User owner = securityUtils.getCurrentUser();

        Booking booking = bookingRepository
                .findByIdAndPropertyOwner(bookingId, owner)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"));

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new BookingException(
                    "Only pending bookings can be confirmed");
        }

        booking.setStatus(BookingStatus.CONFIRMED);

        bookingRepository.save(booking);
    }
}
