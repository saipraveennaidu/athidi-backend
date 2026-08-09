package com.athidi.review.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ReviewResponse(
        Long id,

        Long customerId,

        String customerName,

        Long propertyId,

        Integer rating,

        String comment,

        LocalDateTime createdAt,

        LocalDateTime updatedAt
) {
}
