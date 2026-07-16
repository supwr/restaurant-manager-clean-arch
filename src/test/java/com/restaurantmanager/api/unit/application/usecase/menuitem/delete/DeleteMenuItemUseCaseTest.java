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
import com.restaurantmanager.api.domain.model.Restaurant;

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
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        MenuItem menuItem = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/path");
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(menuItem));

        assertDoesNotThrow(() -> useCase.execute(restaurantUuid, menuItemUuid));

        verify(restaurantGateway, times(1)).findByUuid(restaurantUuid);
        verify(menuItemGateway, times(1)).findByUuid(menuItemUuid);
        verify(menuItemGateway, times(1)).deleteByUuid(menuItemUuid);
    }

    @Test
    void testDeleteMenuItem_RestaurantNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(restaurantGateway, times(1)).findByUuid(restaurantUuid);
        verify(menuItemGateway, never()).findByUuid(any());
        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_MenuItemNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(restaurantGateway, times(1)).findByUuid(restaurantUuid);
        verify(menuItemGateway, times(1)).findByUuid(menuItemUuid);
        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_MenuItemBelongsToOtherRestaurant() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        MenuItem menuItem = new MenuItem(1L, menuItemUuid, 2L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/path");

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(menuItem));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_NullRestaurantId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, UUID.randomUUID()));

        verify(restaurantGateway, never()).findByUuid(any());
        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_NullMenuItemId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(UUID.randomUUID(), null));

        verify(restaurantGateway, never()).findByUuid(any());
        verify(menuItemGateway, never()).deleteByUuid(any());
    }
}

