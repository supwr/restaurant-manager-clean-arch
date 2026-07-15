package com.restaurantmanager.api.unit.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.menuitem.delete.DeleteMenuItemUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private DeleteMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    void testDeleteMenuItem_Success() {
        long restaurantId = 1L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem menuItem = new MenuItem(menuItemId, uuid, restaurantId, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        assertDoesNotThrow(() -> useCase.execute(restaurantId, menuItemId));

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(menuItemGateway, times(1)).findById(menuItemId);
        verify(menuItemGateway, times(1)).deleteById(menuItemId);
    }

    @Test
    void testDeleteMenuItem_RestaurantNotFound() {
        long restaurantId = 999L;
        long menuItemId = 1L;

        when(restaurantGateway.existsById(restaurantId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(menuItemGateway, never()).findById(any());
        verify(menuItemGateway, never()).deleteById(any());
    }

    @Test
    void testDeleteMenuItem_MenuItemNotFound() {
        long restaurantId = 1L;
        long menuItemId = 999L;

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(menuItemGateway, times(1)).findById(menuItemId);
        verify(menuItemGateway, never()).deleteById(any());
    }

    @Test
    void testDeleteMenuItem_MenuItemBelongsToOtherRestaurant() {
        long restaurantId = 1L;
        long otherRestaurantId = 2L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem menuItem = new MenuItem(menuItemId, uuid, otherRestaurantId, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(menuItem));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).deleteById(any());
    }

    @Test
    void testDeleteMenuItem_NullRestaurantId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, 1L));

        verify(restaurantGateway, never()).existsById(any());
        verify(menuItemGateway, never()).deleteById(any());
    }

    @Test
    void testDeleteMenuItem_NullMenuItemId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null));

        verify(restaurantGateway, never()).existsById(any());
        verify(menuItemGateway, never()).deleteById(any());
    }
}

