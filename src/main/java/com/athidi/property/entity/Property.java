package com.athidi.property.entity;

import com.athidi.booking.entity.Booking;
import com.athidi.common.entity.BaseEntity;
import com.athidi.common.enums.PropertyType;
import com.athidi.common.enums.PropertyCategory;
import com.athidi.common.enums.GenderCategory;
import com.athidi.review.entity.Review;
import com.athidi.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "properties")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property extends BaseEntity {
    @Column(nullable = false)
    private String title;

    @Column(length = 2000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PropertyType propertyType;

    @Enumerated(EnumType.STRING)
    private PropertyCategory category;

    @Enumerated(EnumType.STRING)
    private GenderCategory gender;

    @Column(precision = 10, scale = 2)
    private BigDecimal securityDeposit;

    private String noticePeriod;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_images", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "image_url")
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "property_amenities", joinColumns = @JoinColumn(name = "property_id"))
    @Column(name = "amenity_name")
    @Builder.Default
    private List<String> amenities = new ArrayList<>();

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(nullable = false)
    private Integer maxGuests;

    @Column(nullable = false)
    private Integer bedrooms;

    @Column(nullable = false)
    private Integer bathrooms;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String state;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String zipCode;

    @Builder.Default
    private Boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    @OneToMany(
            mappedBy = "property",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();

    @OneToMany(mappedBy = "property")
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();
}
