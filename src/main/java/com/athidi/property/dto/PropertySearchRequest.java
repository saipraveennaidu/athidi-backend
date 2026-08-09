package com.athidi.property.dto;

import com.athidi.common.enums.PropertyType;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PropertySearchRequest {
    private String city;

    private PropertyType propertyType;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;

    private Integer guests;

    private Double minRating;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;
}
