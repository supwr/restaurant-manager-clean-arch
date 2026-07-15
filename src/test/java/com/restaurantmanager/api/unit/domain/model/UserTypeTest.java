package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeTest {

    @Test
    void testCreateUserType_Success() {
        UserType userType = new UserType(1L, "ADMIN", "Administrator user");

        assertNotNull(userType);
        assertEquals(1L, userType.getId());
        assertEquals("ADMIN", userType.getName());
        assertEquals("Administrator user", userType.getObservation());
    }

    @Test
    void testCreateUserType_WithUuid() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(1L, uuid, "ADMIN", "Administrator user");

        assertNotNull(userType);
        assertEquals(uuid, userType.getUuid());
        assertEquals(1L, userType.getId());
    }

    @Test
    void testCreateUserType_WithoutObservation() {
        UserType userType = new UserType(1L, "CUSTOMER", null);

        assertNotNull(userType);
        assertEquals("CUSTOMER", userType.getName());
        assertNull(userType.getObservation());
    }

    @Test
    void testUserTypeValidate_Success() {
        UserType userType = new UserType(1L, "ADMIN", "Administrator");

        assertDoesNotThrow(userType::validate);
    }

    @Test
    void testUserTypeValidate_BlankName() {
        UserType userType = new UserType(1L, "   ", "Administrator");

        ValidationException exception = assertThrows(ValidationException.class, userType::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testUserTypeValidate_NullName() {
        UserType userType = new UserType(1L, null, "Administrator");

        ValidationException exception = assertThrows(ValidationException.class, userType::validate);
        assertEquals("name", exception.getField());
    }
}

