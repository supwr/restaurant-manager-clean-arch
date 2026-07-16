package com.restaurantmanager.api.unit.application.usecase.menuitem.update;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.usecase.menuitem.update.UpdateMenuItemUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
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
class UpdateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    private UpdateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemGateway);
    }

    @Test
    void testUpdateMenuItem_Success() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Old description", new BigDecimal("10.50"), false, "/old");
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "New description", new BigDecimal("12.00"), true, "/new");
        MenuItem updated = new MenuItem(1L, menuItemUuid, 1L, "Burger", "New description", new BigDecimal("12.00"), true, "/new");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.of(existing));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(updated);

        MenuItem result = useCase.execute(restaurantUuid, menuItemUuid, updateData);

        assertNotNull(result);
        assertEquals("Burger", result.getName());
        assertEquals(new BigDecimal("12.00"), result.getPrice());

        verify(menuItemGateway, times(1)).findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid);
        verify(menuItemGateway, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_RestaurantNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemBelongsToOtherRestaurant() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(1L, menuItemUuid, 2L, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_ValidationError_BlankName() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        MenuItem existing = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(null, 1L, "   ", "Description", new BigDecimal("12.00"), true, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertEquals("name", exception.getField());

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_ValidationError_NegativePrice() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        MenuItem existing = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("-5.00"), true, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertEquals("price", exception.getField());

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullRestaurantId() {
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        assertThrows(NullPointerException.class, () -> useCase.execute(null, UUID.randomUUID(), updateData));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullMenuItemId() {
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        assertThrows(NullPointerException.class, () -> useCase.execute(UUID.randomUUID(), null, updateData));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullMenuItem() {
        assertThrows(NullPointerException.class, () -> useCase.execute(UUID.randomUUID(), UUID.randomUUID(), null));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }
}

