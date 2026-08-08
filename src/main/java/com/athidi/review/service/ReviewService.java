package com.athidi.review.service;

import com.athidi.review.dto.CreateReviewRequest;
import com.athidi.review.dto.ReviewResponse;
import org.springframework.data.domain.Page;

public interface ReviewService {
    ReviewResponse createReview(CreateReviewRequest request);

    Page<ReviewResponse> getPropertyReviews(
            Long propertyId,
            int page,
            int size,
            String sortBy
    );

    ReviewResponse updateReview(
            Long reviewId,
            Integer rating,
            String comment
    );

    void deleteReview(Long reviewId);

    Double getAverageRating(Long propertyId);
}
