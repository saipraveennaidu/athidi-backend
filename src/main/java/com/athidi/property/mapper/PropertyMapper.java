package com.athidi.property.mapper;

import com.athidi.property.dto.PropertyResponse;
import com.athidi.property.entity.Property;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PropertyMapper {
    PropertyResponse toResponse(Property property);
}
