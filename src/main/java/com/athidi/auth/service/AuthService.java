package com.athidi.auth.service;

import com.athidi.auth.dto.AuthResponse;
import com.athidi.auth.dto.LoginRequest;
import com.athidi.auth.dto.LoginResponse;
import com.athidi.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    LoginResponse login(LoginRequest request);
}
