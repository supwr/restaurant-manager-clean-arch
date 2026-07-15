package com.restaurantmanager.api.unit.application.usecase.user.delete;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
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
        long userId = 1L;

        when(userGateway.existsById(userId)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(userId));

        verify(userGateway, times(1)).existsById(userId);
        verify(userGateway, times(1)).deleteById(userId);
    }

    @Test
    void testDeleteUser_NotFound() {
        long userId = 999L;

        when(userGateway.existsById(userId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(userId));

        assertTrue(exception.getMessage().contains("User"));
        assertTrue(exception.getMessage().contains("999"));

        verify(userGateway, times(1)).existsById(userId);
        verify(userGateway, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteUser_NullId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).existsById(anyLong());
        verify(userGateway, never()).deleteById(anyLong());
    }
}

