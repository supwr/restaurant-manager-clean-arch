package com.restaurantmanager.api.application.usecase.user.get;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private GetUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetUserUseCase(userGateway);
    }

    @Test
    void testGetUserSuccess() {
        Long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User user = Owner.create(userId, uuid, "John Doe", "john@example.com", "john.doe", true, null, null, now, now);

        when(userGateway.findById(userId)).thenReturn(Optional.of(user));

        User result = useCase.execute(userId);

        assertNotNull(result);
        assertEquals(userId, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("John Doe", result.getName());

        verify(userGateway, times(1)).findById(userId);
    }

    @Test
    void testGetUserNotFound() {
        Long userId = 999L;

        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(userId));

        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("999"));

        verify(userGateway, times(1)).findById(userId);
    }

    @Test
    void testGetUserIdNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).findById(any());
    }
}

