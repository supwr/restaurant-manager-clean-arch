package com.restaurantmanager.api.unit.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = Mappers.getMapper(UserPersistenceMapper.class);

    @Test
    void testToEntityOwner() {
        UUID uuid = UUID.randomUUID();
        User user = Owner.create(1L, uuid, "Owner", "owner@example.com", "owner", true, null, null, Instant.now(), Instant.now());

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(1L, entity.getTypeId());
        assertEquals(uuid, entity.getUuid());
        assertEquals("Owner", entity.getName());
    }

    @Test
    void testToEntityCustomer() {
        User user = Customer.create(1L, UUID.randomUUID(), "Customer", "customer@example.com", "customer", true, null, null, Instant.now(), Instant.now());

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(2L, entity.getTypeId());
    }

    @Test
    void testToDomainOwner() {
        UUID uuid = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("Owner");
        entity.setEmail("owner@example.com");
        entity.setLogin("owner");
        entity.setActive(true);
        entity.setTypeId(1L);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        User user = mapper.toDomain(entity);

        assertTrue(user instanceof Owner);
        assertEquals(uuid, user.getUuid());
    }

    @Test
    void testToDomainCustomer() {
        UUID uuid = UUID.randomUUID();
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("Customer");
        entity.setEmail("customer@example.com");
        entity.setLogin("customer");
        entity.setActive(true);
        entity.setTypeId(2L);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        User user = mapper.toDomain(entity);

        assertTrue(user instanceof Customer);
        assertEquals(uuid, user.getUuid());
    }

    @Test
    void testToDomainNull() {
        assertNull(mapper.toDomain(null));
    }
}

