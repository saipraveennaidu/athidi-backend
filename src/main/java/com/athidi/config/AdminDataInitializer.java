package com.athidi.config;

import com.athidi.common.enums.Role;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminDataInitializer implements CommandLineRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        if (userRepository.existsByRolesContaining(Role.SUPER_ADMIN)) {
            return;
        }

        User admin = User.builder()
                .firstName("System")
                .lastName("Admin")
                .email("superadmin@athidi.com")
                .phoneNumber("9999999988")
                .password(passwordEncoder.encode("Admin@123"))
                .roles(Set.of(Role.SUPER_ADMIN))
                .active(true)
                .build();

        userRepository.save(admin);

        System.out.println("Initial ADMIN account created.");
    }
}
