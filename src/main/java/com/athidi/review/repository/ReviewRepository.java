package com.athidi.review.repository;

import com.athidi.review.entity.Review;
import com.athidi.property.entity.Property;
import com.athidi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>{
    Page<Review> findByProperty(Property property, Pageable pageable);

    boolean existsByBookingId(Long bookingId);

    Page<Review> findByCustomer(User customer, Pageable pageable);

    Optional<Review> findByIdAndCustomer(
            Long reviewId,
            User customer
    );

    @Query("""
        SELECT AVG(r.rating)
        FROM Review r
        WHERE r.property.id = :propertyId
        """)
    Double getAverageRating(@Param("propertyId") Long propertyId);
}
