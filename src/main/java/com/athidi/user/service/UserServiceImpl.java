package com.athidi.user.service;

import com.athidi.common.enums.Role;
import com.athidi.security.SecurityUtils;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    private final SecurityUtils securityUtils;

    @Transactional
    @Override
    public void becomeOwner() {

        User user = securityUtils.getCurrentUser();

        if (!user.getRoles().contains(Role.OWNER)) {

            user.getRoles().add(Role.OWNER);

            userRepository.save(user);
        }
    }
}
