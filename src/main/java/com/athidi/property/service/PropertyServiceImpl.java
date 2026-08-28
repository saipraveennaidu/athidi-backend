package com.athidi.property.service;
import com.athidi.common.validation.PageRequestValidator;
import com.athidi.common.validation.SortFieldValidator;
import com.athidi.exception.BookingException;
import com.athidi.exception.PropertyNotFoundException;
import com.athidi.exception.ResourceNotFoundException;
import com.athidi.property.dto.CreatePropertyRequest;
import com.athidi.property.dto.PropertyResponse;
import com.athidi.property.dto.PropertySearchRequest;
import com.athidi.property.entity.Property;
import com.athidi.property.mapper.PropertyMapper;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.property.specification.PropertySpecification;
import com.athidi.security.SecurityUtils;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;
    private final PropertyMapper propertyMapper;
    private final PageRequestValidator pageRequestValidator;
    private final SortFieldValidator sortFieldValidator;

    @Transactional
    @Override
    public PropertyResponse createProperty(CreatePropertyRequest request) {

        User owner = securityUtils.getCurrentUser();
        log.info("Creating property for owner: {}", owner.getEmail());

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .propertyType(request.getPropertyType())
                .category(request.getCategory())
                .gender(request.getGender())
                .securityDeposit(request.getSecurityDeposit())
                .noticePeriod(request.getNoticePeriod())
                .images(request.getImages() != null ? request.getImages() : new ArrayList<>())
                .amenities(request.getAmenities() != null ? request.getAmenities() : new ArrayList<>())
                .pricePerNight(request.getPricePerNight())
                .maxGuests(request.getMaxGuests())
                .bedrooms(request.getBedrooms())
                .bathrooms(request.getBathrooms())
                .address(request.getAddress())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .zipCode(request.getZipCode())
                .owner(owner)
                .build();

        property = propertyRepository.save(property);
        log.info("Property created successfully with id: {}", property.getId());

        return propertyMapper.toResponse(property);
    }

    @Override
    public List<PropertyResponse> getMyProperties() {

        User owner = securityUtils.getCurrentUser();

        List<Property> properties =
                propertyRepository.findByOwner(owner);

        return properties.stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "properties",
            key = "'property:' + #propertyId"
    )
    public PropertyResponse updateProperty(
            Long propertyId,
            CreatePropertyRequest request) {

        User owner = securityUtils.getCurrentUser();
        log.info("Updating property with id: {}", propertyId);

        Property property = propertyRepository
                .findByIdAndOwner(propertyId, owner)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
        property.setCategory(request.getCategory());
        property.setGender(request.getGender());
        property.setSecurityDeposit(request.getSecurityDeposit());
        property.setNoticePeriod(request.getNoticePeriod());
        property.setImages(request.getImages() != null ? request.getImages() : new ArrayList<>());
        property.setAmenities(request.getAmenities() != null ? request.getAmenities() : new ArrayList<>());
        property.setPricePerNight(request.getPricePerNight());
        property.setMaxGuests(request.getMaxGuests());
        property.setBedrooms(request.getBedrooms());
        property.setBathrooms(request.getBathrooms());
        property.setAddress(request.getAddress());
        property.setCity(request.getCity());
        property.setState(request.getState());
        property.setCountry(request.getCountry());
        property.setZipCode(request.getZipCode());

        property = propertyRepository.save(property);

        return propertyMapper.toResponse(property);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "properties",
            key = "'property:' + #propertyId"
    )
    public void deleteProperty(Long propertyId) {

        User owner = securityUtils.getCurrentUser();
        log.info("Deleting property with id: {}", propertyId);

        Property property = propertyRepository
                .findByIdAndOwner(propertyId, owner)
                .orElseThrow(() ->
                        new PropertyNotFoundException("Property not found"));

        property.setActive(false);

        propertyRepository.save(property);
    }

    @Override
    public Page<PropertyResponse> getAllActiveProperties(
            int page,
            int size,
            String sortBy) {

        pageRequestValidator.validate(page, size);

        sortFieldValidator.validate(
                sortBy,
                Set.of(
                        "id",
                        "title",
                        "pricePerNight",
                        "createdAt"
                )
        );

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        Page<Property> properties =
                propertyRepository.findByActiveTrue(pageable);

        return properties.map(propertyMapper::toResponse);
    }

    @Override
    public Page<PropertyResponse> searchProperties(
            PropertySearchRequest request,
            int page,
            int size,
            String sortBy) {

        pageRequestValidator.validate(page, size);

        sortFieldValidator.validate(
                sortBy,
                Set.of(
                        "id",
                        "title",
                        "pricePerNight",
                        "createdAt"
                )
        );

        if (request.getCheckInDate() != null
                && request.getCheckOutDate() != null
                && !request.getCheckOutDate()
                .isAfter(request.getCheckInDate())) {

            throw new BookingException(
                    "Check-out date must be after check-in date");
        }

        Specification<Property> specification =
                Specification
                        .where(PropertySpecification.isActive())
                        .and(PropertySpecification.hasCategory(
                                request.getCategory()))
                        .and(PropertySpecification.hasGender(
                                request.getGender()))
                        .and(PropertySpecification.hasCity(
                                request.getCity()))
                        .and(PropertySpecification.hasPropertyType(
                                request.getPropertyType()))
                        .and(PropertySpecification.priceBetween(
                                request.getMinPrice(),
                                request.getMaxPrice()))
                        .and(PropertySpecification.canAccommodate(
                                request.getGuests()))
                        .and(PropertySpecification.hasMinimumRating(
                                request.getMinRating()))
                        .and(PropertySpecification.isAvailable(
                                request.getCheckInDate(),
                                request.getCheckOutDate()));

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        return propertyRepository
                .findAll(specification, pageable)
                .map(propertyMapper::toResponse);
    }

    @Override
    @Cacheable(
            value = "properties",
            key = "'property:' + #id"
    )
    public PropertyResponse getPropertyById(Long id) {
        log.info("Fetching property with id: {}", id);
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new PropertyNotFoundException("Property not found with id: " + id));
        return propertyMapper.toResponse(property);
    }

    @Transactional
    @Override
    @CacheEvict(
            value = "properties",
            key = "'property:' + #propertyId"
    )
    public void updatePropertyStatus(Long propertyId, boolean active) {

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found"));

        property.setActive(active);

        propertyRepository.save(property);
    }
}
