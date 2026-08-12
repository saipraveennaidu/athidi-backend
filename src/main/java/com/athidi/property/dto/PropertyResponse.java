package com.athidi.property.dto;

import com.athidi.common.enums.PropertyType;
import com.athidi.common.enums.PropertyCategory;
import com.athidi.common.enums.GenderCategory;
import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record PropertyResponse(
        Long id,

        String title,

        String description,

        PropertyType propertyType,

        PropertyCategory category,

        GenderCategory gender,

        BigDecimal securityDeposit,

        String noticePeriod,

        List<String> images,

        List<String> amenities,

        Double averageRating,

        Integer reviewsCount,

        BigDecimal pricePerNight,

        Integer maxGuests,

        Integer bedrooms,

        Integer bathrooms,

        String city,

        String state,

        String country
) {
}
