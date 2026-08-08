package com.athidi.review.controller;

import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import com.athidi.review.dto.CreateReviewRequest;
import com.athidi.review.dto.ReviewResponse;
import com.athidi.review.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;
    private final ApiResponseBuilder responseBuilder;

    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewResponse>> createReview(
            @Valid @RequestBody CreateReviewRequest request) {

        ReviewResponse response =
                reviewService.createReview(request);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Review created successfully",
                        response
                )
        );
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<ApiResponse<Page<ReviewResponse>>> getPropertyReviews(
            @PathVariable Long propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy) {

        Page<ReviewResponse> response =
                reviewService.getPropertyReviews(
                        propertyId,
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Reviews fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<ReviewResponse>> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody CreateReviewRequest request) {

        ReviewResponse response =
                reviewService.updateReview(
                        id,
                        request.getRating(),
                        request.getComment()
                );

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Review updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ApiResponse<String>> deleteReview(
            @PathVariable Long id) {

        reviewService.deleteReview(id);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Review deleted successfully",
                        "Deleted"
                )
        );
    }

    @GetMapping("/property/{propertyId}/rating")
    public ResponseEntity<ApiResponse<Double>> getAverageRating(
            @PathVariable Long propertyId) {

        Double rating =
                reviewService.getAverageRating(propertyId);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Average rating fetched successfully",
                        rating
                )
        );
    }
}
