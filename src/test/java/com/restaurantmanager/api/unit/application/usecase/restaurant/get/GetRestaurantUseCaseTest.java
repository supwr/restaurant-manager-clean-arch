package com.restaurantmanager.api.unit.application.usecase.restaurant.get;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private GetRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetRestaurantUseCase(restaurantGateway);
    }

    @Test
    void testExecute_Success() {
        Restaurant restaurant = new Restaurant(1L, UUID.randomUUID(), "Resto", "Street", "Italian", "9AM", 1L);
        when(restaurantGateway.findById(1L)).thenReturn(Optional.of(restaurant));

        Restaurant result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(restaurantGateway).findById(1L);
    }

    @Test
    void testExecute_NotFound() {
        when(restaurantGateway.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L));

        assertTrue(exception.getMessage().contains("Restaurant"));
        verify(restaurantGateway).findById(1L);
    }

    @Test
    void testExecute_NullId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(restaurantGateway, never()).findById(any());
    }
}

