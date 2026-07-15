package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class OwnerTest {

    @Test
    void testCreateOwner_Success() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        Owner owner = Owner.create(1L, uuid, "John Doe", "john@example.com", "johndoe", true, "password123", null, now, now);

        assertNotNull(owner);
        assertEquals(1L, owner.getId());
        assertEquals(uuid, owner.getUuid());
        assertEquals("John Doe", owner.getName());
        assertEquals("john@example.com", owner.getEmail());
        assertEquals("johndoe", owner.getLogin());
        assertEquals(true, owner.getActive());
        assertEquals("password123", owner.getPassword());
    }

    @Test
    void testCreateOwner_WithoutId() {
        Owner owner = Owner.create(null, null, "John Doe", "john@example.com", "johndoe", true, null, null, null, null);

        assertNotNull(owner);
        assertNull(owner.getId());
        assertNull(owner.getUuid());
        assertEquals("John Doe", owner.getName());
    }

    @Test
    void testCreateOwner_ExtendsUser() {
        UUID uuid = UUID.randomUUID();
        Owner owner = Owner.create(1L, uuid, "John", "john@example.com", "john", true, null, null, null, null);

        assertTrue(owner instanceof User);
    }

    @Test
    void testCreateOwner_WithAllNullFields() {
        Owner owner = Owner.create(null, null, null, null, null, true, null, null, null, null);

        assertNotNull(owner);
        assertNull(owner.getId());
        assertNull(owner.getName());
        assertNull(owner.getEmail());
        assertNull(owner.getLogin());
    }
}

