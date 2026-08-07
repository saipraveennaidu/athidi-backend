package com.athidi.property.service;
import com.athidi.exception.ResourceNotFoundException;
import com.athidi.property.dto.CreatePropertyRequest;
import com.athidi.property.dto.PropertyResponse;
import com.athidi.property.entity.Property;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    @Override
    public PropertyResponse createProperty(CreatePropertyRequest request) {

        Authentication authentication = SecurityContextHolder
                .getContext()
                .getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Property property = Property.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .propertyType(request.getPropertyType())
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

        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .propertyType(property.getPropertyType())
                .pricePerNight(property.getPricePerNight())
                .maxGuests(property.getMaxGuests())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .build();
    }

    @Override
    public List<PropertyResponse> getMyProperties() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        List<Property> properties =
                propertyRepository.findByOwner(owner);

        return properties.stream()
                .map(property -> PropertyResponse.builder()
                        .id(property.getId())
                        .title(property.getTitle())
                        .description(property.getDescription())
                        .propertyType(property.getPropertyType())
                        .pricePerNight(property.getPricePerNight())
                        .maxGuests(property.getMaxGuests())
                        .bedrooms(property.getBedrooms())
                        .bathrooms(property.getBathrooms())
                        .city(property.getCity())
                        .state(property.getState())
                        .country(property.getCountry())
                        .build())
                .toList();
    }

    @Override
    public PropertyResponse updateProperty(
            Long propertyId,
            CreatePropertyRequest request) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Property property = propertyRepository
                .findByIdAndOwner(propertyId, owner)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found"));

        property.setTitle(request.getTitle());
        property.setDescription(request.getDescription());
        property.setPropertyType(request.getPropertyType());
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

        return PropertyResponse.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .propertyType(property.getPropertyType())
                .pricePerNight(property.getPricePerNight())
                .maxGuests(property.getMaxGuests())
                .bedrooms(property.getBedrooms())
                .bathrooms(property.getBathrooms())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .build();
    }

    @Override
    public void deleteProperty(Long propertyId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User owner = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        Property property = propertyRepository
                .findByIdAndOwner(propertyId, owner)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Property not found"));

        property.setActive(false);

        propertyRepository.save(property);
    }

//    @Override
//    public List<PropertyResponse> getAllActiveProperties() {
//
//        List<Property> properties = propertyRepository.findByActiveTrue();
//
//        return properties.stream()
//                .map(property -> PropertyResponse.builder()
//                        .id(property.getId())
//                        .title(property.getTitle())
//                        .description(property.getDescription())
//                        .propertyType(property.getPropertyType())
//                        .pricePerNight(property.getPricePerNight())
//                        .maxGuests(property.getMaxGuests())
//                        .bedrooms(property.getBedrooms())
//                        .bathrooms(property.getBathrooms())
//                        .city(property.getCity())
//                        .state(property.getState())
//                        .country(property.getCountry())
//                        .build())
//                .toList();
//    }

    @Override
    public Page<PropertyResponse> getAllActiveProperties(
            int page,
            int size,
            String sortBy) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortBy).ascending()
        );

        Page<Property> properties =
                propertyRepository.findByActiveTrue(pageable);

        return properties.map(property ->
                PropertyResponse.builder()
                        .id(property.getId())
                        .title(property.getTitle())
                        .description(property.getDescription())
                        .propertyType(property.getPropertyType())
                        .pricePerNight(property.getPricePerNight())
                        .maxGuests(property.getMaxGuests())
                        .bedrooms(property.getBedrooms())
                        .bathrooms(property.getBathrooms())
                        .city(property.getCity())
                        .state(property.getState())
                        .country(property.getCountry())
                        .build()
        );
    }
}
