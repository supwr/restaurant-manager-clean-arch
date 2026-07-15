package com.restaurantmanager.api.unit.application.usecase.menuitem.get;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.menuitem.get.GetMenuItemUseCase;
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
class GetMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private GetMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    void testExecute_Success() {
        MenuItem menuItem = new MenuItem(1L, UUID.randomUUID(), 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.existsById(1L)).thenReturn(true);
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));

        MenuItem result = useCase.execute(1L, 1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(menuItemGateway).findById(1L);
    }

    @Test
    void testExecute_RestaurantNotFound() {
        when(restaurantGateway.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L, 1L));

        assertTrue(exception.getMessage().contains("Restaurant"));
        verify(menuItemGateway, never()).findById(any());
    }

    @Test
    void testExecute_MenuItemNotFound() {
        when(restaurantGateway.existsById(1L)).thenReturn(true);
        when(menuItemGateway.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L, 1L));

        assertTrue(exception.getMessage().contains("MenuItem"));
    }

    @Test
    void testExecute_MenuItemFromDifferentRestaurant() {
        MenuItem menuItem = new MenuItem(1L, UUID.randomUUID(), 2L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.existsById(1L)).thenReturn(true);
        when(menuItemGateway.findById(1L)).thenReturn(Optional.of(menuItem));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L, 1L));

        assertTrue(exception.getMessage().contains("MenuItem"));
    }

    @Test
    void testExecute_NullRestaurantId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, 1L));
        verify(menuItemGateway, never()).findById(any());
    }
}

