package com.athidi.property.specification;

import com.athidi.booking.entity.Booking;
import com.athidi.common.enums.BookingStatus;
import com.athidi.common.enums.PropertyType;
import com.athidi.property.entity.Property;
import com.athidi.review.entity.Review;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PropertySpecification {
    public static Specification<Property> hasCity(String city) {

        return (root, query, criteriaBuilder) ->
                city == null || city.isBlank()
                        ? null
                        : criteriaBuilder.equal(
                        criteriaBuilder.lower(root.get("city")),
                        city.toLowerCase()
                );
    }

    public static Specification<Property> hasPropertyType(
            PropertyType propertyType) {

        return (root, query, criteriaBuilder) ->
                propertyType == null
                        ? null
                        : criteriaBuilder.equal(
                        root.get("propertyType"),
                        propertyType
                );
    }

    public static Specification<Property> priceBetween(
            BigDecimal minPrice,
            BigDecimal maxPrice) {

        return (root, query, criteriaBuilder) -> {

            if (minPrice != null && maxPrice != null) {
                return criteriaBuilder.between(
                        root.get("pricePerNight"),
                        minPrice,
                        maxPrice
                );
            }

            if (minPrice != null) {
                return criteriaBuilder.greaterThanOrEqualTo(
                        root.get("pricePerNight"),
                        minPrice
                );
            }

            if (maxPrice != null) {
                return criteriaBuilder.lessThanOrEqualTo(
                        root.get("pricePerNight"),
                        maxPrice
                );
            }

            return null;
        };
    }

    public static Specification<Property> canAccommodate(
            Integer guests) {

        return (root, query, criteriaBuilder) ->
                guests == null
                        ? null
                        : criteriaBuilder.greaterThanOrEqualTo(
                        root.get("maxGuests"),
                        guests
                );
    }

    public static Specification<Property> isActive() {

        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isTrue(root.get("active"));
    }

    public static Specification<Property> hasMinimumRating(
            Double minRating) {

        return (root, query, cb) -> {

            if (minRating == null) {
                return null;
            }

            Join<Property, Review> reviews =
                    root.join("reviews", JoinType.LEFT);

            query.groupBy(root.get("id"));

            query.having(
                    cb.greaterThanOrEqualTo(
                            cb.avg(reviews.get("rating")),
                            minRating
                    )
            );

            return cb.conjunction();
        };
    }

    public static Specification<Property> isAvailable(
            LocalDate checkInDate,
            LocalDate checkOutDate) {

        return (root, query, cb) -> {

            if (checkInDate == null || checkOutDate == null) {
                return null;
            }

            Subquery<Long> subquery = query.subquery(Long.class);

            Root<Booking> bookingRoot =
                    subquery.from(Booking.class);

            subquery.select(bookingRoot.get("id"));

            subquery.where(
                    cb.and(
                            cb.equal(
                                    bookingRoot.get("property"),
                                    root
                            ),

                            cb.equal(
                                    bookingRoot.get("status"),
                                    BookingStatus.CONFIRMED
                            ),

                            cb.lessThan(
                                    bookingRoot.get("checkInDate"),
                                    checkOutDate
                            ),

                            cb.greaterThan(
                                    bookingRoot.get("checkOutDate"),
                                    checkInDate
                            )
                    )
            );

            return cb.not(cb.exists(subquery));
        };
    }
}

