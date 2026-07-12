package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void validateValidMenuItem() {
        MenuItem m = MenuItem.builder()
            .restaurantId(1L)
            .name("Margherita")
            .description("Classic pizza")
            .price(new BigDecimal("12.50"))
            .localOnly(false)
            .photoPath("/images/margherita.jpg")
            .build();

        assertDoesNotThrow(m::validate);
    }

    @Test
    void validateMissingRestaurant() {
        MenuItem m = MenuItem.builder()
            .restaurantId(null)
            .name("Margherita")
            .description("Classic pizza")
            .price(new BigDecimal("12.50"))
            .localOnly(false)
            .photoPath("/images/margherita.jpg")
            .build();

        assertThrows(ValidationException.class, m::validate);
    }

    @Test
    void validateNegativePrice() {
        MenuItem m = MenuItem.builder()
            .restaurantId(1L)
            .name("Cheap")
            .description("Invalid")
            .price(new BigDecimal("-1.00"))
            .localOnly(false)
            .photoPath("")
            .build();

        assertThrows(ValidationException.class, m::validate);
    }
}

