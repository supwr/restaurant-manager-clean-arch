package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void validateValidRestaurant() {
        Restaurant r = Restaurant.builder()
            .name("Pizza House")
            .address("123 Main St")
            .cuisineType("Italian")
            .openingHours("9am-10pm")
            .ownerUserId(1L)
            .build();

        assertDoesNotThrow(r::validate);
    }

    @Test
    void validateMissingName() {
        Restaurant r = Restaurant.builder()
            .name("")
            .address("123 Main St")
            .cuisineType("Italian")
            .openingHours("9am-10pm")
            .ownerUserId(1L)
            .build();

        assertThrows(ValidationException.class, r::validate);
    }

    @Test
    void validateMissingOwner() {
        Restaurant r = Restaurant.builder()
            .name("Pizza")
            .address("123")
            .cuisineType("Italian")
            .openingHours("9-22")
            .ownerUserId(null)
            .build();

        assertThrows(ValidationException.class, r::validate);
    }
}

