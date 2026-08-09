package com.athidi.auth.controller;

import com.athidi.auth.dto.AuthResponse;
import com.athidi.auth.dto.LoginRequest;
import com.athidi.auth.dto.LoginResponse;
import com.athidi.auth.dto.RegisterRequest;
import com.athidi.auth.service.AuthService;
import com.athidi.common.response.ApiResponse;
import com.athidi.common.response.ApiResponseBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final ApiResponseBuilder responseBuilder;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(
                responseBuilder.success(
                        "Registration Successful",
                        response
                )
        );
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        LoginResponse response =
                authService.login(request);

        return ResponseEntity.ok(
                responseBuilder.success(
                        "Login Successful",
                        response
                )
        );
    }
}
