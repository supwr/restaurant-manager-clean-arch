package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void testCreateUserType_Success() {
        UserType userType = new UserType(1L, "ADMIN");

        assertNotNull(userType);
        assertEquals(1L, userType.getId());
        assertEquals("ADMIN", userType.getName());
    }

    @Test
    void testCreateUserType_WithUuid() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(1L, uuid, "ADMIN");

        assertNotNull(userType);
        assertEquals(uuid, userType.getUuid());
        assertEquals(1L, userType.getId());
    }

    @Test
    void testCreateUserType_WithoutObservation() {
        UserType userType = new UserType(1L, "CUSTOMER");

        assertNotNull(userType);
        assertEquals("CUSTOMER", userType.getName());
    }

    @Test
    void testUserTypeValidate_Success() {
        UserType userType = new UserType(1L, "ADMIN");

        assertDoesNotThrow(userType::validate);
    }

    @Test
    void testUserTypeValidate_BlankName() {
        UserType userType = new UserType(1L, "   ");

        ValidationException exception = assertThrows(ValidationException.class, userType::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testUserTypeValidate_NullName() {
        UserType userType = new UserType(1L, UUID.randomUUID(), null);

        ValidationException exception = assertThrows(ValidationException.class, userType::validate);
        assertEquals("name", exception.getField());
    }
}

