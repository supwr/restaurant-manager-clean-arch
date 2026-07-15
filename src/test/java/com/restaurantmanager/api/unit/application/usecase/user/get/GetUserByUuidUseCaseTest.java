package com.restaurantmanager.api.unit.application.usecase.user.get;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.get.GetUserByUuidUseCase;
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
import static org.mockito.ArgumentMatchers.any;
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
    void testExecute_Success() {
        UUID uuid = UUID.randomUUID();
        User user = Owner.create(1L, uuid, "John Doe", "john@example.com", "john", true, null, null, Instant.now(), Instant.now());

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(user));

        User result = useCase.execute(uuid);

        assertNotNull(result);
        assertEquals(uuid, result.getUuid());
        verify(userGateway).findByUuid(uuid);
    }

    @Test
    void testExecute_NotFound() {
        UUID uuid = UUID.randomUUID();
        when(userGateway.findByUuid(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(uuid));

        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains(uuid.toString()));
        verify(userGateway).findByUuid(uuid);
    }

    @Test
    void testExecute_NullUuid() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(userGateway, never()).findByUuid(any());
    }
}

