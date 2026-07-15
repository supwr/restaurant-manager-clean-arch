package com.restaurantmanager.api.unit.application.usecase.menuitem.update;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
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

    @Mock
    private RestaurantGateway restaurantGateway;

    private UpdateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    void testUpdateMenuItem_Success() {
        long restaurantId = 1L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(menuItemId, uuid, restaurantId, "Pizza", "Old description", new BigDecimal("10.50"), false, "/old");
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "Burger", "New description", new BigDecimal("12.00"), true, "/new");
        MenuItem updated = new MenuItem(menuItemId, uuid, restaurantId, "Burger", "New description", new BigDecimal("12.00"), true, "/new");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(existing));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(updated);

        MenuItem result = useCase.execute(restaurantId, menuItemId, updateData);

        assertNotNull(result);
        assertEquals("Burger", result.getName());
        assertEquals(new BigDecimal("12.00"), result.getPrice());

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(menuItemGateway, times(1)).findById(menuItemId);
        verify(menuItemGateway, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_RestaurantNotFound() {
        long restaurantId = 999L;
        long menuItemId = 1L;
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId, updateData));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemNotFound() {
        long restaurantId = 1L;
        long menuItemId = 999L;
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemBelongsToOtherRestaurant() {
        long restaurantId = 1L;
        long otherRestaurantId = 2L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(menuItemId, uuid, otherRestaurantId, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(existing));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, menuItemId, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_ValidationError_BlankName() {
        long restaurantId = 1L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(menuItemId, uuid, restaurantId, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "   ", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, menuItemId, updateData));

        assertEquals("name", exception.getField());

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_ValidationError_NegativePrice() {
        long restaurantId = 1L;
        long menuItemId = 1L;
        UUID uuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(menuItemId, uuid, restaurantId, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(menuItemId, restaurantId, "Burger", "Description", new BigDecimal("-5.00"), true, "/path");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findById(menuItemId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, menuItemId, updateData));

        assertEquals("price", exception.getField());

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullRestaurantId() {
        MenuItem updateData = new MenuItem(1L, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        assertThrows(NullPointerException.class, () -> useCase.execute(null, 1L, updateData));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullMenuItemId() {
        MenuItem updateData = new MenuItem(1L, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null, updateData));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_NullMenuItem() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, 1L, null));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }
}

