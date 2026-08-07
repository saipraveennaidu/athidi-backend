package com.athidi.user.controller;

import com.athidi.common.response.ApiResponse;
import com.athidi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/become-owner")
    public ResponseEntity<ApiResponse<String>> becomeOwner() {

        userService.becomeOwner();

        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .success(true)
                        .message("You are now an OWNER")
                        .data("Role upgraded successfully")
                        .timestamp(LocalDateTime.now())
                        .build()
        );
    }
}
