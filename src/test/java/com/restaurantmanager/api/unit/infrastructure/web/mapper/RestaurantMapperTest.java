package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantMapper;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantMapperTest {

    private final RestaurantMapper mapper = Mappers.getMapper(RestaurantMapper.class);

    @Test
    void testMapDomainToResponse() {
        UUID uuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Test Restaurant", "123 Main St", "Italian", "9AM-10PM", 1L);

        RestaurantResponse response = mapper.map(domain);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(uuid, response.getUuid());
        assertEquals("Test Restaurant", response.getName());
    }

    @Test
    void testMapRequestToDomain() {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("New Restaurant");
        request.setAddress("456 Oak Ave");
        request.setCuisineType("French");
        request.setOpeningHours("10AM-11PM");
        request.setOwnerUserId(2L);

        Restaurant domain = mapper.map(request);

        assertNotNull(domain);
        assertNull(domain.getId());
        assertEquals("New Restaurant", domain.getName());
    }

    @Test
    void testMapRequestToUpdateDomain() {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("Updated Restaurant");
        request.setAddress("789 Elm St");
        request.setCuisineType("Spanish");
        request.setOpeningHours("12PM-12AM");
        request.setOwnerUserId(3L);

        Restaurant domain = mapper.map(1L, request);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Updated Restaurant", domain.getName());
    }

    @Test
    void testMapNullRequest() {
        assertNull(mapper.map((RestaurantRequest) null));
    }
}

