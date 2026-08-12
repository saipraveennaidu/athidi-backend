package com.athidi.property.dto;

import com.athidi.common.enums.PropertyType;
import com.athidi.common.enums.PropertyCategory;
import com.athidi.common.enums.GenderCategory;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
    private PropertyCategory category;

    @NotNull
    private GenderCategory gender;

    @DecimalMin("0.0")
    private BigDecimal securityDeposit;

    private String noticePeriod;

    private List<String> images;

    private List<String> amenities;

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
