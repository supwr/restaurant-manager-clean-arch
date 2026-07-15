package com.restaurantmanager.api.unit.application.usecase.restaurant.update;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
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
class UpdateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private UpdateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateRestaurantUseCase(restaurantGateway);
    }

    @Test
    void testUpdateRestaurant_Success() {
        long restaurantId = 1L;
        UUID uuid = UUID.randomUUID();

        Restaurant existing = new Restaurant(restaurantId, uuid, "Old Name", "Old Address", "Italian", "9AM-10PM", 1L);
        Restaurant updateData = new Restaurant(restaurantId, "New Name", "New Address", "French", "10AM-11PM", 1L);
        Restaurant updated = new Restaurant(restaurantId, uuid, "New Name", "New Address", "French", "10AM-11PM", 1L);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));
        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(updated);

        Restaurant result = useCase.execute(restaurantId, updateData);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("New Address", result.getAddress());
        assertEquals("French", result.getCuisineType());

        verify(restaurantGateway, times(1)).findById(restaurantId);
        verify(restaurantGateway, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_NotFound() {
        long restaurantId = 999L;
        Restaurant updateData = new Restaurant(restaurantId, "New Name", "New Address", "French", "10AM-11PM", 1L);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, updateData));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(restaurantGateway, times(1)).findById(restaurantId);
        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_ValidationError_BlankName() {
        long restaurantId = 1L;
        UUID uuid = UUID.randomUUID();

        Restaurant existing = new Restaurant(restaurantId, uuid, "Old Name", "Old Address", "Italian", "9AM-10PM", 1L);
        Restaurant updateData = new Restaurant(restaurantId, "   ", "New Address", "French", "10AM-11PM", 1L);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, updateData));

        assertEquals("name", exception.getField());

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_ValidationError_NullAddress() {
        long restaurantId = 1L;
        UUID uuid = UUID.randomUUID();

        Restaurant existing = new Restaurant(restaurantId, uuid, "Old Name", "Old Address", "Italian", "9AM-10PM", 1L);
        Restaurant updateData = new Restaurant(restaurantId, "New Name", null, "French", "10AM-11PM", 1L);

        when(restaurantGateway.findById(restaurantId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, updateData));

        assertEquals("address", exception.getField());

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_NullId() {
        Restaurant updateData = new Restaurant(1L, "New Name", "New Address", "French", "10AM-11PM", 1L);

        assertThrows(NullPointerException.class, () -> useCase.execute(null, updateData));

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }

    @Test
    void testUpdateRestaurant_NullRestaurant() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null));

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }
}

