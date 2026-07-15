package com.restaurantmanager.api.unit.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.persistence.entity.MenuItemEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemPersistenceMapperTest {

    private final MenuItemPersistenceMapper mapper = Mappers.getMapper(MenuItemPersistenceMapper.class);

    @Test
    void testToEntity() {
        UUID uuid = UUID.randomUUID();
        MenuItem domain = new MenuItem(1L, uuid, 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");

        MenuItemEntity entity = mapper.toEntity(domain);

        assertNotNull(entity);
        assertEquals(1L, entity.getId());
        assertEquals(uuid, entity.getUuid());
        assertEquals(10L, entity.getRestaurantId());
    }

    @Test
    void testToDomain() {
        UUID uuid = UUID.randomUUID();
        MenuItemEntity entity = new MenuItemEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setRestaurantId(10L);
        entity.setName("Pizza");
        entity.setDescription("Tasty");
        entity.setPrice(new BigDecimal("10.50"));
        entity.setLocalOnly(true);
        entity.setPhotoPath("/pizza");

        MenuItem domain = mapper.toDomain(entity);

        assertNotNull(domain);
        assertEquals(uuid, domain.getUuid());
        assertEquals(10L, domain.getRestaurantId());
    }

    @Test
    void testToDomainNull() {
        assertNull(mapper.toDomain(null));
    }
}

