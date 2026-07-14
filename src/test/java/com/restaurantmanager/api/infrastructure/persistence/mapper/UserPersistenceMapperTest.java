package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private UserPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(UserPersistenceMapper.class);
    }

    @Test
    void testToEntityOwner() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User owner = Owner.create(1L, uuid, "John Doe", "john@example.com", "john.doe", true, null, null, now, now);

        UserEntity entity = mapper.toEntity(owner);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(uuid, entity.getUuid());
        assertEquals("John Doe", entity.getName());
        assertEquals("john@example.com", entity.getEmail());
        assertEquals("john.doe", entity.getLogin());
        assertEquals(true, entity.getActive());
        assertEquals(1L, entity.getTypeId());
    }

    @Test
    void testToEntityCustomer() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User customer = Customer.create(2L, uuid, "Jane Doe", "jane@example.com", "jane.doe", true, null, null, now, now);

        UserEntity entity = mapper.toEntity(customer);

        assertNotNull(entity);
        assertEquals(2L, entity.getId());
        assertEquals(uuid, entity.getUuid());
        assertEquals("Jane Doe", entity.getName());
        assertEquals("jane@example.com", entity.getEmail());
        assertEquals("jane.doe", entity.getLogin());
        assertEquals(true, entity.getActive());
        assertEquals(2L, entity.getTypeId());
    }

    @Test
    void testToDomainOwner() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("John Doe");
        entity.setEmail("john@example.com");
        entity.setLogin("john.doe");
        entity.setActive(true);
        entity.setTypeId(1L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertInstanceOf(Owner.class, domain);
        assertEquals(1L, domain.getId());
        assertEquals(uuid, domain.getUuid());
        assertEquals("John Doe", domain.getName());
        assertEquals("john@example.com", domain.getEmail());
        assertEquals("john.doe", domain.getLogin());
    }

    @Test
    void testToDomainCustomer() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        UserEntity entity = new UserEntity();
        entity.setId(2L);
        entity.setUuid(uuid);
        entity.setName("Jane Doe");
        entity.setEmail("jane@example.com");
        entity.setLogin("jane.doe");
        entity.setActive(true);
        entity.setTypeId(2L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        User domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertInstanceOf(Customer.class, domain);
        assertEquals(2L, domain.getId());
        assertEquals(uuid, domain.getUuid());
        assertEquals("Jane Doe", domain.getName());
    }

    @Test
    void testToDomainNull() {
        User domain = mapper.toDomain(null);
        assertNull(domain);
    }
}

