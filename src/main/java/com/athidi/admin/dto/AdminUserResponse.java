package com.athidi.admin.dto;

import com.athidi.common.enums.Role;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Set;

@Builder
public record AdminUserResponse(
        Long id,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        Set<Role> roles,
        Boolean active,
        LocalDateTime createdAt
) {
}
