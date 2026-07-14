package com.restaurantmanager.api.application.usecase.user.list;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserCaseTest {

    @Mock
    private UserGateway userGateway;

    private ListUserCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListUserCase(userGateway);
    }

    @Test
    void testListUsersFirstPage() {
        Instant now = Instant.now();
        List<User> users = List.of(
            Owner.create(1L, UUID.randomUUID(), "User 1", "user1@example.com", "user1", true, null, null, now, now),
            Owner.create(2L, UUID.randomUUID(), "User 2", "user2@example.com", "user2", true, null, null, now, now),
            Customer.create(3L, UUID.randomUUID(), "User 3", "user3@example.com", "user3", true, null, null, now, now),
            Customer.create(4L, UUID.randomUUID(), "User 4", "user4@example.com", "user4", true, null, null, now, now)
        );

        when(userGateway.findAll()).thenReturn(users);

        Pagination pagination = new Pagination(0, 2, "name");
        PageResult<User> result = useCase.execute(pagination);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(4L, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals("User 1", result.getContent().get(0).getName());
        assertEquals("User 2", result.getContent().get(1).getName());

        verify(userGateway, times(1)).findAll();
    }

    @Test
    void testListUsersSecondPage() {
        Instant now = Instant.now();
        List<User> users = List.of(
            Owner.create(1L, UUID.randomUUID(), "User 1", "user1@example.com", "user1", true, null, null, now, now),
            Owner.create(2L, UUID.randomUUID(), "User 2", "user2@example.com", "user2", true, null, null, now, now),
            Customer.create(3L, UUID.randomUUID(), "User 3", "user3@example.com", "user3", true, null, null, now, now),
            Customer.create(4L, UUID.randomUUID(), "User 4", "user4@example.com", "user4", true, null, null, now, now)
        );

        when(userGateway.findAll()).thenReturn(users);

        Pagination pagination = new Pagination(1, 2, "name");
        PageResult<User> result = useCase.execute(pagination);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(4L, result.getTotalElements());
        assertEquals(2, result.getTotalPages());
        assertEquals("User 3", result.getContent().get(0).getName());
        assertEquals("User 4", result.getContent().get(1).getName());
    }

    @Test
    void testListUsersEmpty() {
        when(userGateway.findAll()).thenReturn(List.of());

        Pagination pagination = new Pagination(0, 10, "name");
        PageResult<User> result = useCase.execute(pagination);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0L, result.getTotalElements());
        assertEquals(0, result.getTotalPages());

        verify(userGateway, times(1)).findAll();
    }

    @Test
    void testListUsersInvalidPageSize() {
        Pagination pagination = new Pagination(0, 0, "name");

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(pagination));

        assertEquals("size", exception.getField());
        assertTrue(exception.getMessage().contains("Page size must be greater than zero"));

        verify(userGateway, never()).findAll();
    }

    @Test
    void testListUsersPaginationNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).findAll();
    }
}

