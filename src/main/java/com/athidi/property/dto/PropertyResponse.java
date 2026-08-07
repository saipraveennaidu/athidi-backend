package com.athidi.property.dto;

import com.athidi.common.enums.PropertyType;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record PropertyResponse(
        Long id,

        String title,

        String description,

        PropertyType propertyType,

        BigDecimal pricePerNight,

        Integer maxGuests,

        Integer bedrooms,

        Integer bathrooms,

        String city,

        String state,

        String country
) {
}
