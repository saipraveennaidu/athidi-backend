package com.athidi.booking.repository;

import com.athidi.booking.entity.Booking;
import com.athidi.common.enums.BookingStatus;
import com.athidi.property.entity.Property;
import com.athidi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long>{
    Page<Booking> findByCustomer(User customer, Pageable pageable);

    boolean existsByPropertyAndStatusAndCheckInDateLessThanAndCheckOutDateGreaterThan(
            Property property,
            BookingStatus status,
            LocalDate checkOutDate,
            LocalDate checkInDate
    );

    Optional<Booking> findByIdAndCustomer(
            Long bookingId,
            User customer
    );

    Page<Booking> findByPropertyOwner(User owner, Pageable pageable);

    Optional<Booking> findByIdAndPropertyOwner(
            Long bookingId,
            User owner
    );
}
