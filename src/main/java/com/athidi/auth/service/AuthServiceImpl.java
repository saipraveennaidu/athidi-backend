package com.athidi.auth.service;

import com.athidi.auth.dto.AuthResponse;
import com.athidi.auth.dto.LoginRequest;
import com.athidi.auth.dto.LoginResponse;
import com.athidi.auth.dto.RegisterRequest;
import com.athidi.common.entity.User;
import com.athidi.common.enums.Role;
import com.athidi.exception.EmailAlreadyExistsException;
import com.athidi.exception.PhoneNumberAlreadyExistsException;
import com.athidi.security.jwt.JwtService;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Override
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        if (request.getPhoneNumber() != null &&
                userRepository.existsByPhoneNumber(request.getPhoneNumber())) {

            throw new PhoneNumberAlreadyExistsException("Phone number already exists");
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .phoneNumber(request.getPhoneNumber())
                .roles(new HashSet<>())
                .build();

        user.getRoles().add(Role.CUSTOMER);

        userRepository.save(user);

        return AuthResponse.builder()
                .message("User registered successfully")
                .build();
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(

                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );

        UserDetails userDetails =
                (UserDetails) authentication.getPrincipal();

        String jwtToken =
                jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(jwtToken)
                .build();
    }
}
