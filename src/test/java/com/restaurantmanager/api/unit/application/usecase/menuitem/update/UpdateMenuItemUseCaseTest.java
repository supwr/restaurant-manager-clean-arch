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
import com.restaurantmanager.api.domain.model.Restaurant;

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
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        MenuItem existing = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Old description", new BigDecimal("10.50"), false, "/old");
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "New description", new BigDecimal("12.00"), true, "/new");
        MenuItem updated = new MenuItem(1L, menuItemUuid, 1L, "Burger", "New description", new BigDecimal("12.00"), true, "/new");

        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(existing));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(updated);

        MenuItem result = useCase.execute(restaurantUuid, menuItemUuid, updateData);

        assertNotNull(result);
        assertEquals("Burger", result.getName());
        assertEquals(new BigDecimal("12.00"), result.getPrice());

        verify(restaurantGateway, times(1)).findByUuid(restaurantUuid);
        verify(menuItemGateway, times(1)).findByUuid(menuItemUuid);
        verify(menuItemGateway, times(1)).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_RestaurantNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid, updateData));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).save(any(MenuItem.class));
    }

    @Test
    void testUpdateMenuItem_MenuItemBelongsToOtherRestaurant() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        MenuItem existing = new MenuItem(1L, menuItemUuid, 2L, "Pizza", "Description", new BigDecimal("10.50"), false, "/path");
        MenuItem updateData = new MenuItem(null, 1L, "Burger", "Description", new BigDecimal("12.00"), true, "/path");

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(existing));

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

        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(existing));

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

        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(existing));

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

