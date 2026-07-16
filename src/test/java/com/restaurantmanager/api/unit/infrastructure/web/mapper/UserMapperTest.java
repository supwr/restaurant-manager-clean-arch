package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.web.mapper.UserMapper;
import com.restaurantmanager.api.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testMapUserToResponse() {
        UUID uuid = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T10:00:00Z");

        com.restaurantmanager.api.domain.model.UserType domainType = new com.restaurantmanager.api.domain.model.UserType(1L, UUID.randomUUID(), "OWNER");
        User user = new User(1L, uuid, "Owner", "owner@example.com", "owner", true, domainType, createdAt, updatedAt);

        UserResponse response = mapper.map(user);

        assertNotNull(response);
        assertEquals(uuid, response.getUuid());
        assertEquals("Owner", response.getName());
        assertEquals("owner@example.com", response.getEmail());
        assertEquals("OWNER", response.getType().getName());
    }

    @Test
    void testMapCustomerToResponse() {
        UUID uuid = UUID.randomUUID();
        com.restaurantmanager.api.domain.model.UserType domainType = new com.restaurantmanager.api.domain.model.UserType(2L, UUID.randomUUID(), "CUSTOMER");
        User user = new User(2L, uuid, "Customer", "customer@example.com", "customer", true, domainType, null, null);

        UserResponse response = mapper.map(user);

        assertNotNull(response);
        assertEquals("CUSTOMER", response.getType().getName());
    }

    @Test
    void testMapNullUser() {
        assertNull(mapper.map((User) null));
    }

    @Test
    void testMapNullUserType() {
        UUID uuid = UUID.randomUUID();
        User user = new User(1L, uuid, "Name", "email@example.com", "login", true, null, null, null);

        UserResponse response = mapper.map(user);

        assertNotNull(response);
        assertNull(response.getType());
    }
}

