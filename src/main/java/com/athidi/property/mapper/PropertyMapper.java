package com.athidi.property.mapper;

import com.athidi.property.dto.PropertyResponse;
import com.athidi.property.entity.Property;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    @Mapping(target = "averageRating", expression = "java(calculateAverageRating(property))")
    @Mapping(target = "reviewsCount", expression = "java(property.getReviews() != null ? property.getReviews().size() : 0)")
    PropertyResponse toResponse(Property property);

    default Double calculateAverageRating(Property property) {
        if (property.getReviews() == null || property.getReviews().isEmpty()) {
            return 0.0;
        }
        return property.getReviews().stream()
                .mapToDouble(review -> review.getRating())
                .average()
                .orElse(0.0);
    }
}
