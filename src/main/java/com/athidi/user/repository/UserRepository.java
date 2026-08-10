package com.athidi.user.repository;

import com.athidi.common.enums.Role;
import com.athidi.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByPhoneNumber(String phoneNumber);

    Page<User> findAllByOrderByCreatedAtDesc(Pageable pageable);

    boolean existsByRolesContaining(Role role);

    long countByRolesContaining(Role role);
}
