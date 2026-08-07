package com.athidi.property.controller;

import com.athidi.common.response.ApiResponse;
import com.athidi.property.dto.CreatePropertyRequest;
import com.athidi.property.dto.PropertyResponse;
import com.athidi.property.service.PropertyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/properties")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;

    @PostMapping
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<PropertyResponse>> createProperty(
            @Valid @RequestBody CreatePropertyRequest request) {

        PropertyResponse response =
                propertyService.createProperty(request);

        return ResponseEntity.ok(
                ApiResponse.<PropertyResponse>builder()
                        .success(true)
                        .message("Property created successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getMyProperties() {

        List<PropertyResponse> response =
                propertyService.getMyProperties();

        return ResponseEntity.ok(
                ApiResponse.<List<PropertyResponse>>builder()
                        .success(true)
                        .message("Properties fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<PropertyResponse>> updateProperty(
            @PathVariable Long id,
            @Valid @RequestBody CreatePropertyRequest request) {

        PropertyResponse response =
                propertyService.updateProperty(id, request);

        return ResponseEntity.ok(
                ApiResponse.<PropertyResponse>builder()
                        .success(true)
                        .message("Property updated successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<ApiResponse<String>> deleteProperty(
            @PathVariable Long id) {

        propertyService.deleteProperty(id);

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("Property deleted successfully")
                        .data("Deleted")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<PropertyResponse>>> getAllProperties() {
//
//        List<PropertyResponse> response =
//                propertyService.getAllActiveProperties();
//
//        return ResponseEntity.ok(
//                ApiResponse.<List<PropertyResponse>>builder()
//                        .success(true)
//                        .message("Properties fetched successfully")
//                        .data(response)
//                        .timestamp(LocalDateTime.now())
//                        .build()
//        );
//    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PropertyResponse>>> getAllProperties(

            @RequestParam(defaultValue = "0") int page,

            @RequestParam(defaultValue = "10") int size,

            @RequestParam(defaultValue = "id") String sortBy
    ) {

        Page<PropertyResponse> response =
                propertyService.getAllActiveProperties(
                        page,
                        size,
                        sortBy
                );

        return ResponseEntity.ok(
                ApiResponse.<Page<PropertyResponse>>builder()
                        .success(true)
                        .message("Properties fetched successfully")
                        .data(response)
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
