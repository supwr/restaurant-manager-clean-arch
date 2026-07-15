package com.restaurantmanager.api.unit.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.persistence.entity.RestaurantEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantPersistenceMapperTest {

    private RestaurantPersistenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = Mappers.getMapper(RestaurantPersistenceMapper.class);
    }

    @Test
    void testToDomain_Success() {
        UUID uuid = UUID.randomUUID();
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("Pizzeria");
        entity.setAddress("123 Main St");
        entity.setCuisineType("Italian");
        entity.setOpeningHours("9AM-10PM");
        entity.setOwnerUserId(1L);

        Restaurant result = mapper.toDomain(entity);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("Pizzeria", result.getName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("Italian", result.getCuisineType());
        assertEquals("9AM-10PM", result.getOpeningHours());
        assertEquals(1L, result.getOwnerUserId());
    }

    @Test
    void testToDomain_Null() {
        Restaurant result = mapper.toDomain(null);

        assertNull(result);
    }

    @Test
    void testToEntity_Success() {
        UUID uuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Pizzeria", "123 Main St", "Italian", "9AM-10PM", 1L);

        RestaurantEntity result = mapper.toEntity(domain);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("Pizzeria", result.getName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("Italian", result.getCuisineType());
        assertEquals("9AM-10PM", result.getOpeningHours());
        assertEquals(1L, result.getOwnerUserId());
    }

    @Test
    void testToEntity_Null() {
        RestaurantEntity result = mapper.toEntity(null);

        assertNull(result);
    }

    @Test
    void testMapping_Bidirectional() {
        UUID uuid = UUID.randomUUID();
        RestaurantEntity entity = new RestaurantEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("Pizzeria");
        entity.setAddress("123 Main St");
        entity.setCuisineType("Italian");
        entity.setOpeningHours("9AM-10PM");
        entity.setOwnerUserId(1L);

        Restaurant domain = mapper.toDomain(entity);
        RestaurantEntity mappedBack = mapper.toEntity(domain);

        assertEquals(entity.getId(), mappedBack.getId());
        assertEquals(entity.getUuid(), mappedBack.getUuid());
        assertEquals(entity.getName(), mappedBack.getName());
        assertEquals(entity.getAddress(), mappedBack.getAddress());
        assertEquals(entity.getCuisineType(), mappedBack.getCuisineType());
    }
}

