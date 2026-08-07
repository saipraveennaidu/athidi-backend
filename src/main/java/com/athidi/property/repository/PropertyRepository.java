package com.athidi.property.repository;

import com.athidi.property.entity.Property;
import com.athidi.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long>{
    List<Property> findByOwner(User owner);
    Optional<Property> findByIdAndOwner(Long id, User owner);
    Page<Property> findByActiveTrue(Pageable pageable);
}
