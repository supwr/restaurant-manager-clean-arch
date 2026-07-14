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
class GetUserByUuidUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private GetUserByUuidUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetUserByUuidUseCase(userGateway);
    }

    @Test
    void testGetUserByUuidSuccess() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User user = Owner.create(1L, uuid, "John Doe", "john@example.com", "john.doe", true, null, null, now, now);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(user));

        User result = useCase.execute(uuid);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("John Doe", result.getName());

        verify(userGateway, times(1)).findByUuid(uuid);
    }

    @Test
    void testGetUserByUuidNotFound() {
        UUID uuid = UUID.randomUUID();

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(uuid));

        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains(uuid.toString()));

        verify(userGateway, times(1)).findByUuid(uuid);
    }

    @Test
    void testGetUserByUuidNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).findByUuid(any());
    }
}

