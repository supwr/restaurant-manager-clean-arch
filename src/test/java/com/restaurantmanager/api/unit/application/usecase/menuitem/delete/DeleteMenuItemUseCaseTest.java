package com.restaurantmanager.api.unit.application.usecase.menuitem.delete;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    private DeleteMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteMenuItemUseCase(menuItemGateway);
    }

    @Test
    void testDeleteMenuItem_Success() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();
        final MenuItem menuItem = new MenuItem(1L, menuItemUuid, 1L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/path");

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.of(menuItem));

        assertDoesNotThrow(() -> useCase.execute(restaurantUuid, menuItemUuid));

        verify(menuItemGateway, times(1)).findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid);
        verify(menuItemGateway, times(1)).deleteByUuid(menuItemUuid);
    }

    @Test
    void testDeleteMenuItem_NotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        final UUID menuItemUuid = UUID.randomUUID();

        when(menuItemGateway.findByUuidAndRestaurantUuid(menuItemUuid, restaurantUuid)).thenReturn(Optional.empty());

        final EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, menuItemUuid));

        assertTrue(exception.getMessage().contains("MenuItem"));

        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_NullRestaurantId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, UUID.randomUUID()));

        verify(menuItemGateway, never()).deleteByUuid(any());
    }

    @Test
    void testDeleteMenuItem_NullMenuItemId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(UUID.randomUUID(), null));

        verify(menuItemGateway, never()).deleteByUuid(any());
    }
}

