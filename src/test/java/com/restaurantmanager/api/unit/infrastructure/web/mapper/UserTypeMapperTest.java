package com.restaurantmanager.api.unit.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.web.mapper.UserTypeMapper;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.*;

class UserTypeMapperTest {

    private final UserTypeMapper mapper = Mappers.getMapper(UserTypeMapper.class);

    @Test
    void testMapDomainToResponse() {
        UserType domain = new UserType(1L, java.util.UUID.randomUUID(), "ADMIN", "Administrator");

        UserTypeResponse response = mapper.map(domain);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("ADMIN", response.getName());
    }

    @Test
    void testMapRequestToDomain() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("CUSTOMER");
        request.setObservation("Customer role");

        UserType domain = mapper.map(request);

        assertNotNull(domain);
        assertNull(domain.getId());
        assertEquals("CUSTOMER", domain.getName());
    }

    @Test
    void testMapRequestToUpdateDomain() {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("MANAGER");
        request.setObservation("Manager role");

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

