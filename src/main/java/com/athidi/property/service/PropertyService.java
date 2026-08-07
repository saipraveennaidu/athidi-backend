package com.athidi.property.service;

import com.athidi.property.dto.CreatePropertyRequest;
import com.athidi.property.dto.PropertyResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface PropertyService {
    PropertyResponse createProperty(CreatePropertyRequest request);
    List<PropertyResponse> getMyProperties();
    PropertyResponse updateProperty(
            Long propertyId,
            CreatePropertyRequest request
    );
    void deleteProperty(Long propertyId);
//    List<PropertyResponse> getAllActiveProperties();
    Page<PropertyResponse> getAllActiveProperties(
            int page,
            int size,
            String sortBy
    );
}
