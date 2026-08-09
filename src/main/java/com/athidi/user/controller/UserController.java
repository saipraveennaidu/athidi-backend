package com.athidi.user.controller;

import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import com.athidi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ApiResponseBuilder responseBuilder;

    @PostMapping("/become-owner")
    public ResponseEntity<ApiResponse<String>> becomeOwner() {

        userService.becomeOwner();

        return ResponseEntity.ok(
                responseBuilder.success(
                        "You are now an OWNER",
                        "Role upgraded successfully"
                )
        );
    }
}
