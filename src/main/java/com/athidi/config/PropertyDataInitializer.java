package com.athidi.config;

import com.athidi.common.enums.Role;
import com.athidi.common.enums.PropertyType;
import com.athidi.common.enums.PropertyCategory;
import com.athidi.common.enums.GenderCategory;
import com.athidi.property.entity.Property;
import com.athidi.property.repository.PropertyRepository;
import com.athidi.user.entity.User;
import com.athidi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PropertyDataInitializer implements CommandLineRunner {
    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (propertyRepository.count() > 0) {
            return;
        }

        // Get or create Host Owner
        User owner = userRepository.findByEmail("owner@athidi.com")
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .firstName("Host")
                            .lastName("Owner")
                            .email("owner@athidi.com")
                            .phoneNumber("9999999977")
                            .password(passwordEncoder.encode("Owner@123"))
                            .roles(Set.of(Role.OWNER))
                            .active(true)
                            .build();
                    return userRepository.save(newUser);
                });

        // 1. Sunrise Girls PG
        Property pg = Property.builder()
                .title("Sunrise Girls PG")
                .description("Clean and safe paying guest accommodation near Hitech City. Daily housekeeping, high speed WiFi, laundry services, and highly nutritious home-cooked meals included in monthly fee.")
                .propertyType(PropertyType.HOMESTAY)
                .category(PropertyCategory.PG)
                .gender(GenderCategory.FEMALE)
                .pricePerNight(new BigDecimal("500.00")) // Daily stay fallback
                .securityDeposit(new BigDecimal("5000.00"))
                .noticePeriod("15 Days Notice")
                .maxGuests(4)
                .bedrooms(2)
                .bathrooms(2)
                .address("Plot 12, Phase 2, Hitech City")
                .city("Hyderabad")
                .state("Telangana")
                .country("India")
                .zipCode("500081")
                .active(true)
                .owner(owner)
                .images(List.of(
                        "https://images.unsplash.com/photo-1522771739844-6a9f6d5f14af?auto=format&fit=crop&w=800&q=80",
                        "https://images.unsplash.com/photo-1586023492125-27b2c045efd7?auto=format&fit=crop&w=800&q=80",
                        "https://images.unsplash.com/photo-1556911220-e15b29be8c8f?auto=format&fit=crop&w=800&q=80"
                ))
                .amenities(List.of(
                        "Food Available", "High Speed WiFi", "Furnished Rooms", "RO Water", "24x7 Security", "Washing Machine", "Power Backup", "Housekeeping"
                ))
                .build();

        // 2. 2BHK Miyapur Flat
        Property rent = Property.builder()
                .title("2BHK Semi-Furnished Flat")
                .description("Spacious 2BHK apartment in Miyapur. Close to metro station, with 24x7 water supply, power backup, covered parking, and balconies facing open green space.")
                .propertyType(PropertyType.APARTMENT)
                .category(PropertyCategory.RENT)
                .gender(GenderCategory.ANY)
                .pricePerNight(new BigDecimal("12000.00")) // Monthly equivalent price represented in Price field
                .securityDeposit(new BigDecimal("24000.00"))
                .noticePeriod("1 Month Notice")
                .maxGuests(6)
                .bedrooms(2)
                .bathrooms(2)
                .address("Block C, Green Meadows Apartments, Miyapur")
                .city("Hyderabad")
                .state("Telangana")
                .country("India")
                .zipCode("500049")
                .active(true)
                .owner(owner)
                .images(List.of(
                        "https://images.unsplash.com/photo-1560448204-e02f11c3d0e2?auto=format&fit=crop&w=800&q=80",
                        "https://images.unsplash.com/photo-1502672260266-1c1ef2d93688?auto=format&fit=crop&w=800&q=80"
                ))
                .amenities(List.of(
                        "WiFi", "Covered Parking", "Air Conditioning", "Washing Machine", "Power Backup", "Lift"
                ))
                .build();

        // 3. 3BHK Luxury Apartment Gachibowli
        Property buy = Property.builder()
                .title("3BHK Luxury Apartment")
                .description("Vastu compliant premium 3BHK apartment in gated community, Gachibowli. Offers world-class clubhouse, swimming pool, gymnasium, children play areas, and multi-tier security.")
                .propertyType(PropertyType.APARTMENT)
                .category(PropertyCategory.BUY)
                .gender(GenderCategory.ANY)
                .pricePerNight(new BigDecimal("8500000.00")) // Buy price
                .securityDeposit(new BigDecimal("0.00"))
                .noticePeriod("None")
                .maxGuests(8)
                .bedrooms(3)
                .bathrooms(3)
                .address("Tower 5, Gachibowli Heights, Gachibowli")
                .city("Hyderabad")
                .state("Telangana")
                .country("India")
                .zipCode("500032")
                .active(true)
                .owner(owner)
                .images(List.of(
                        "https://images.unsplash.com/photo-1545324418-cc1a3fa10c00?auto=format&fit=crop&w=800&q=80",
                        "https://images.unsplash.com/photo-1484154218962-a197022b5858?auto=format&fit=crop&w=800&q=80"
                ))
                .amenities(List.of(
                        "Swimming Pool", "Gymnasium", "24x7 Security", "Power Backup", "Clubhouse", "Elevator", "Gas Pipeline"
                ))
                .build();

        propertyRepository.save(pg);
        propertyRepository.save(rent);
        propertyRepository.save(buy);

        System.out.println("Initial Athidi seed properties created (Sunrise Girls PG, Miyapur Flat, Gachibowli Luxury Apartment).");
    }
}
