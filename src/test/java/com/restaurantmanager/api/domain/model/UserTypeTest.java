package com.restaurantmanager.api.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void testValidateWithValidName() {
        UserType userType = UserType.builder()
            .id(1L)
            .name("Restaurant Owner")
            .observation("Owner of a restaurant")
            .build();

        // Should not throw exception
        assertDoesNotThrow(userType::validate);
    }

    @Test
    void testValidateWithNullName() {
        UserType userType = UserType.builder()
            .id(1L)
            .name(null)
            .observation("Owner of a restaurant")
            .build();

        assertThrows(ValidationException.class, userType::validate);
    }

    @Test
    void testValidateWithBlankName() {
        UserType userType = UserType.builder()
            .id(1L)
            .name("   ")
            .observation("Owner of a restaurant")
            .build();

        assertThrows(ValidationException.class, userType::validate);
    }

    @Test
    void testValidateWithEmptyName() {
        UserType userType = UserType.builder()
            .id(1L)
            .name("")
            .observation("Owner of a restaurant")
            .build();

        assertThrows(ValidationException.class, userType::validate);
    }

    @Test
    void testUserTypeBuilderAndGetters() {
        UserType userType = UserType.builder()
            .id(1L)
            .name("Customer")
            .observation("Regular customer")
            .build();

        assertEquals(1L, userType.getId());
        assertEquals("Customer", userType.getName());
        assertEquals("Regular customer", userType.getObservation());
    }
}

