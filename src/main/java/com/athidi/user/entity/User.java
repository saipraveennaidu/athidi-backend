package com.athidi.user.entity;

import com.athidi.booking.entity.Booking;
import com.athidi.common.entity.BaseEntity;
import com.athidi.common.enums.Role;
import com.athidi.property.entity.Property;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity{
    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;

    @Column(unique = true)
    private String phoneNumber;

    @Builder.Default
    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Builder.Default
    private Boolean active = true;

    @Builder.Default
    private Boolean emailVerified = false;

    @OneToMany(
            mappedBy = "owner",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Property> properties = new ArrayList<>();

    @OneToMany(
            mappedBy = "customer",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<Booking> bookings = new ArrayList<>();
}
