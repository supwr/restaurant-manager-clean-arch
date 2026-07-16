package com.restaurantmanager.api.unit.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserPersistenceMapperTest {

    private final UserPersistenceMapper mapper = Mappers.getMapper(UserPersistenceMapper.class);

    @Test
    void testToEntityWithUserType() {
        UUID uuid = UUID.randomUUID();
        UserType userType = new UserType(1L, UUID.randomUUID(), "OWNER");
        User user = new User(1L, uuid, "Owner", "owner@example.com", "owner", true, userType, Instant.now(), Instant.now());

        UserEntity entity = mapper.toEntity(user);

        assertNotNull(entity);
        assertEquals(1L, entity.getTypeId());
        assertEquals(uuid, entity.getUuid());
        assertEquals("Owner", entity.getName());
    }

    @Test
    void testToDomainWithUserType() {
        UUID uuid = UUID.randomUUID();
        UUID typeUuid = UUID.randomUUID();
        UserTypeEntity typeEntity = new UserTypeEntity();
        typeEntity.setId(1L);
        typeEntity.setUuid(typeUuid);
        typeEntity.setName("OWNER");

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("Owner");
        entity.setEmail("owner@example.com");
        entity.setLogin("owner");
        entity.setActive(true);
        entity.setType(typeEntity);
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());

        User user = mapper.toDomain(entity);

        assertNotNull(user);
        assertNotNull(user.getUserType());
        assertEquals(typeUuid, user.getUserType().getUuid());
        assertEquals("OWNER", user.getUserType().getName());
        assertEquals(uuid, user.getUuid());
    }

    @Test
    void testToDomainNull() {
        assertNull(mapper.toDomain(null));
    }
}

