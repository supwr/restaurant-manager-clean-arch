package com.restaurantmanager.api.unit.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTypePersistenceMapperTest {

    private final UserTypePersistenceMapper mapper = Mappers.getMapper(UserTypePersistenceMapper.class);

    @Test
    void testToEntity() {
        UUID uuid = UUID.randomUUID();
        UserType domain = new UserType(1L, uuid, "ADMIN", "Administrator");

        UserTypeEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(uuid, entity.getUuid());
        assertEquals("ADMIN", entity.getName());
    }

    @Test
    void testToDomain() {
        UUID uuid = UUID.randomUUID();
        UserTypeEntity entity = new UserTypeEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("ADMIN");
        entity.setObservation("Administrator");

        UserType domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(uuid, domain.getUuid());
        assertEquals("ADMIN", domain.getName());
    }

    @Test
    void testToDomainNull() {
        assertNull(mapper.toDomain(null));
    }
}

