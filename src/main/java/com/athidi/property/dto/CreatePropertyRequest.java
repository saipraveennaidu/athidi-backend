package com.athidi.property.dto;

import com.athidi.common.enums.PropertyType;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePropertyRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private PropertyType propertyType;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal pricePerNight;

    @NotNull
    @Min(1)
    private Integer maxGuests;

    @NotNull
    @Min(1)
    private Integer bedrooms;

    @NotNull
    @Min(1)
    private Integer bathrooms;

    @NotBlank
    private String address;

    @NotBlank
    private String city;

    @NotBlank
    private String state;

    @NotBlank
    private String country;

    @NotBlank
    private String zipCode;
}
