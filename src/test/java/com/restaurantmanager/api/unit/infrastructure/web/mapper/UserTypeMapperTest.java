package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.web.mapper.UserTypeMapper;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.openapitools.jackson.nullable.JsonNullable;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeMapperTest {

    private final UserTypeMapper mapper = Mappers.getMapper(UserTypeMapper.class);

    @Test
    void testMapDomainToResponse() {
        java.util.UUID uuid = java.util.UUID.randomUUID();
        UserType domain = new UserType(1L, uuid, "ADMIN");

        UserTypeResponse response = mapper.map(domain);

        assertNotNull(response);
        assertEquals(uuid, response.getUuid());
        assertEquals("ADMIN", response.getName());
    }

    @Test
    void testMapRequestToDomain() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("CUSTOMER");

        UserType domain = mapper.map(request);

        assertNotNull(domain);
        assertNull(domain.getId());
        assertEquals("CUSTOMER", domain.getName());
    }

    @Test
    void testMapRequestToUpdateDomain() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("MANAGER");

        UserType domain = mapper.map(10L, request);

        assertNotNull(domain);
        assertEquals(10L, domain.getId());
        assertEquals("MANAGER", domain.getName());
    }

    @Test
    void testMapNullRequest() {
        assertNull(mapper.map((UserTypeRequest) null));
    }
}

