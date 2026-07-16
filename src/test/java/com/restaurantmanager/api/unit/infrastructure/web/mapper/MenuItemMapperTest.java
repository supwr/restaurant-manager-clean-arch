package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.web.mapper.MenuItemMapper;
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import com.restaurantmanager.api.model.RelatedRestaurant;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemMapperTest {

    private final MenuItemMapper mapper = Mappers.getMapper(MenuItemMapper.class);

    @Test
    void testMapDomainToResponse() {
        MenuItem domain = new MenuItem(1L, UUID.randomUUID(), 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");

        RelatedRestaurant restaurant = new RelatedRestaurant();
        restaurant.setId(UUID.randomUUID());
        restaurant.setName("Restaurant");
        MenuItemResponse response = mapper.map(domain, restaurant);

        assertNotNull(response);
        assertEquals(restaurant, response.getRestaurant());
        assertEquals("Pizza", response.getName());
        assertEquals(new BigDecimal("10.50"), response.getPrice());
    }

    @Test
    void testMapRequestToDomain() {
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Burger");
        request.setDescription("Tasty burger");
        request.setPrice(new BigDecimal("12.00"));
        request.setLocalOnly(false);
        request.setPhotoPath("/burger");

        MenuItem domain = mapper.map(10L, request);

        assertNotNull(domain);
        assertNull(domain.getId());
        assertEquals(10L, domain.getRestaurantId());
        assertEquals("Burger", domain.getName());
    }

    @Test
    void testMapRequestToUpdateDomain() {
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Burger");
        request.setDescription("Tasty burger");
        request.setPrice(new BigDecimal("12.00"));
        request.setLocalOnly(false);
        request.setPhotoPath("/burger");

        MenuItem domain = mapper.map(10L, 2L, request);

        assertNotNull(domain);
        assertEquals(2L, domain.getId());
        assertEquals(10L, domain.getRestaurantId());
    }

    @Test
    void testMapNullRequest() {
        assertNull(mapper.map(10L, null));
    }
}

