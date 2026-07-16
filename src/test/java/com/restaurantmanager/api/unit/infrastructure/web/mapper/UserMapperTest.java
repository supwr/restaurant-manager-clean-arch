package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Address;
import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.web.mapper.UserMapper;
import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.model.UserType;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private final UserMapper mapper = Mappers.getMapper(UserMapper.class);

    @Test
    void testMapOwnerToResponse() {
        UUID uuid = UUID.randomUUID();
        Instant createdAt = Instant.parse("2026-01-01T10:00:00Z");
        Instant updatedAt = Instant.parse("2026-01-02T10:00:00Z");
        Address address = new Address("Street", 10L, "City", "12345");
        User user = Owner.create(1L, uuid, "Owner", "owner@example.com", "owner", true, null, address, createdAt, updatedAt);
        UserType type = new UserType();
        UUID typeUuid = UUID.randomUUID();
        type.setUuid(typeUuid);
        type.setName("OWNER");

        UserResponse response = mapper.map(user, type);

        assertNotNull(response);
        assertEquals(uuid, response.getUuid());
        assertEquals("Owner", response.getName());
        assertEquals("owner@example.com", response.getEmail());
        assertEquals("OWNER", response.getType().getName());
        assertEquals(typeUuid, response.getType().getUuid());
    }

    @Test
    void testMapCustomerToResponse() {
        UUID uuid = UUID.randomUUID();
        User user = Customer.create(2L, uuid, "Customer", "customer@example.com", "customer", true, null, null, null, null);

        UserResponse response = mapper.map(user);

        assertNotNull(response);
        assertEquals("CUSTOMER", response.getType().getName());
    }

    @Test
    void testMapNullUser() {
        assertNull(mapper.map((User) null));
    }

    @Test
    void testMapUnsupportedUserSubtypeThrows() {
        User unsupported = new User(1L, UUID.randomUUID(), "Name", "email@example.com", "login", true, null, null, null, null) {};
        assertThrows(IllegalArgumentException.class, () -> mapper.map(unsupported));
    }
}

