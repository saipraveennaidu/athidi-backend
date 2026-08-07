package com.athidi.user.service;

import com.athidi.common.enums.Role;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;

    @Override
    public void becomeOwner() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found"));

        if (!user.getRoles().contains(Role.OWNER)) {

            user.getRoles().add(Role.OWNER);

            userRepository.save(user);
        }
    }
}
