package com.restaurantmanager.api.unit.application.usecase.user.list;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.list.ListUserCase;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private ListUserCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListUserCase(userGateway);
    }

    @Test
    void testExecute_Success() {
        User user1 = new User(1L, UUID.randomUUID(), "John", "john@example.com", "john", true, null, null, null);
        User user2 = new User(2L, UUID.randomUUID(), "Jane", "jane@example.com", "jane", true, null, null, null);
        when(userGateway.findAll()).thenReturn(List.of(user1, user2));

        PageResult<User> result = useCase.execute(new Pagination(0, 20, "id"));

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(userGateway).findAll();
    }

    @Test
    void testExecute_Paginated() {
        User user1 = new User(1L, UUID.randomUUID(), "John", "john@example.com", "john", true, null, null, null);
        User user2 = new User(2L, UUID.randomUUID(), "Jane", "jane@example.com", "jane", true, null, null, null);
        User user3 = new User(3L, UUID.randomUUID(), "Bob", "bob@example.com", "bob", true, null, null, null);
        when(userGateway.findAll()).thenReturn(List.of(user1, user2, user3));

        PageResult<User> result = useCase.execute(new Pagination(1, 2, "id"));

        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(1, result.getContent().size());
        assertEquals(2, result.getTotalPages());
    }

    @Test
    void testExecute_InvalidPageSize() {
        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(new Pagination(0, 0, "id")));
        assertEquals("size", exception.getField());
        verify(userGateway, never()).findAll();
    }

    @Test
    void testExecute_NullPagination() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(userGateway, never()).findAll();
    }
}

