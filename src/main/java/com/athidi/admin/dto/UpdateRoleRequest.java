package com.athidi.admin.dto;

import com.athidi.common.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateRoleRequest {
    @NotNull
    private Role role;
}
