package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantMapper;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import com.restaurantmanager.api.model.RelatedUser;
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
        RelatedUser owner = new RelatedUser();
        owner.setId(UUID.randomUUID());
        owner.setName("Owner");

        RestaurantResponse response = mapper.map(domain, owner);

        assertNotNull(response);
        assertEquals(uuid, response.getUuid());
        assertEquals("Test Restaurant", response.getName());
        assertEquals(owner, response.getOwner());
    }

    @Test
    void testMapRequestToDomain() {
        RestaurantRequest request = new RestaurantRequest();
        request.setName("New Restaurant");
        request.setAddress("456 Oak Ave");
        request.setCuisineType("French");
        request.setOpeningHours("10AM-11PM");
        com.restaurantmanager.api.model.OwnerRequest ownerReq = new com.restaurantmanager.api.model.OwnerRequest();
        ownerReq.setId(UUID.randomUUID());
        request.setOwner(ownerReq);

        Restaurant domain = mapper.map(2L, request);

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
        com.restaurantmanager.api.model.OwnerRequest ownerReq2 = new com.restaurantmanager.api.model.OwnerRequest();
        ownerReq2.setId(UUID.randomUUID());
        request.setOwner(ownerReq2);

        Restaurant domain = mapper.map(1L, 3L, request);

        assertNotNull(domain);
        assertEquals(1L, domain.getId());
        assertEquals("Updated Restaurant", domain.getName());
    }

    @Test
    void testMapNullRequest() {
        assertNull(mapper.map(1L, (RestaurantRequest) null));
    }
}

