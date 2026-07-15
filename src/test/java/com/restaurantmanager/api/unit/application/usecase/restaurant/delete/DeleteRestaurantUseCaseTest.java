package com.restaurantmanager.api.unit.application.usecase.restaurant.delete;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
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
class DeleteRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private DeleteRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteRestaurantUseCase(restaurantGateway);
    }

    @Test
    void testDeleteRestaurant_Success() {
        long restaurantId = 1L;

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(restaurantId));

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(restaurantGateway, times(1)).deleteById(restaurantId);
    }

    @Test
    void testDeleteRestaurant_NotFound() {
        long restaurantId = 999L;

        when(restaurantGateway.existsById(restaurantId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(restaurantGateway, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteRestaurant_NullId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(restaurantGateway, never()).existsById(anyLong());
        verify(restaurantGateway, never()).deleteById(anyLong());
    }
}

