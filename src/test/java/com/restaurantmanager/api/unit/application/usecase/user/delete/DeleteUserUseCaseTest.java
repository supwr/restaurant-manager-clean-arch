package com.restaurantmanager.api.unit.application.usecase.user.delete;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserUseCase;
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
class DeleteUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private DeleteUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserUseCase(userGateway);
    }

    @Test
    void testDeleteUser_Success() {
        UUID uuid = UUID.randomUUID();
        long userId = 1L;
        User user = Owner.create(userId, uuid, "Test User", "test@example.com", "testuser", true, null, null, Instant.now(), Instant.now());

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> useCase.execute(uuid));

        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        UUID uuid = UUID.randomUUID();

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(uuid));

        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains(uuid.toString()));

        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, never()).deleteById(any());
    }

    @Test
    void testDeleteUser_NullUuid() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).findByUuid(any());
        verify(userGateway, never()).deleteById(any());
    }
}

