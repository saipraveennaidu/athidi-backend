package com.athidi.review.service;

import com.athidi.booking.entity.Booking;
import com.athidi.booking.repository.BookingRepository;
import com.athidi.common.enums.BookingStatus;
import com.athidi.common.validation.PageRequestValidator;
import com.athidi.common.validation.SortFieldValidator;
import com.athidi.exception.BookingException;
import com.athidi.exception.ResourceNotFoundException;
import com.athidi.property.entity.Property;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.review.dto.CreateReviewRequest;
import com.athidi.review.dto.ReviewResponse;
import com.athidi.review.entity.Review;
import com.athidi.review.repository.ReviewRepository;
import com.athidi.security.SecurityUtils;
import com.athidi.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
    private final ReviewRepository reviewRepository;
    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final SecurityUtils securityUtils;
    private final PageRequestValidator pageRequestValidator;
    private final SortFieldValidator sortFieldValidator;

    @Transactional
    @Override
    public ReviewResponse createReview(CreateReviewRequest request) {

        // 1. Get logged-in customer
        User customer = securityUtils.getCurrentUser();

        // 2. Find booking
        Booking booking = bookingRepository
                .findByIdAndCustomer(request.getBookingId(), customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Booking not found"));

        // 3. Booking must be completed
        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new BookingException(
                    "You can review a property only after completing your booking");
        }

        // 4. Prevent duplicate review
        if (reviewRepository.existsByBookingId(booking.getId())) {
            throw new BookingException(
                    "This booking has already been reviewed");
        }

        // 5. Create review
        Review review = Review.builder()
                .customer(customer)
                .property(booking.getProperty())
                .booking(booking)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        review = reviewRepository.save(review);

        return mapToResponse(review);
    }

    @Override
    public Page<ReviewResponse> getPropertyReviews(
            Long propertyId,
            int page,
            int size,
            String sortBy) {

        pageRequestValidator.validate(page, size);

        sortFieldValidator.validate(
                sortBy,
                Set.of(
                        "id",
                        "rating",
                        "createdAt",
                        "updatedAt"
                )
        );

        Property property = propertyRepository
                .findById(propertyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Property not found"));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).descending()
        );

        return reviewRepository
                .findByProperty(property, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    @Override
    public ReviewResponse updateReview(
            Long reviewId,
            Integer rating,
            String comment) {

        User customer = securityUtils.getCurrentUser();

        Review review = reviewRepository
                .findByIdAndCustomer(reviewId, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        review.setRating(rating);
        review.setComment(comment);

        review = reviewRepository.save(review);

        return mapToResponse(review);
    }

    @Transactional
    @Override
    public void deleteReview(Long reviewId) {

        User customer = securityUtils.getCurrentUser();

        Review review = reviewRepository
                .findByIdAndCustomer(reviewId, customer)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }

    @Override
    public Double getAverageRating(Long propertyId) {

        if (!propertyRepository.existsById(propertyId)) {
            throw new ResourceNotFoundException("Property not found");
        }

        Double average =
                reviewRepository.getAverageRating(propertyId);

        return average != null ? average : 0.0;
    }

    private ReviewResponse mapToResponse(Review review) {

        return ReviewResponse.builder()
                .id(review.getId())
                .customerId(review.getCustomer().getId())
                .customerName(review.getCustomer().getFirstName())
                .propertyId(review.getProperty().getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}
