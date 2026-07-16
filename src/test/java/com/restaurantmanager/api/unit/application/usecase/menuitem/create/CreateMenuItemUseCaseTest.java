package com.restaurantmanager.api.unit.application.usecase.menuitem.create;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.menuitem.create.CreateMenuItemUseCase;
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
class CreateMenuItemUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private CreateMenuItemUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    void testExecute_Success() {
        final UUID restaurantUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        MenuItem input = new MenuItem(null, 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        MenuItem saved = new MenuItem(1L, UUID.randomUUID(), 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");

        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.save(any(MenuItem.class))).thenReturn(saved);

        MenuItem result = useCase.execute(restaurantUuid, input);

        assertNotNull(result);
        verify(menuItemGateway).save(any(MenuItem.class));
    }

    @Test
    void testExecute_RestaurantNotFound() {
        final UUID restaurantUuid = UUID.randomUUID();
        MenuItem input = new MenuItem(null, 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantUuid, input));

        assertTrue(exception.getMessage().contains("Restaurant"));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void testExecute_InvalidMenuItem() {
        final UUID restaurantUuid = UUID.randomUUID();
        final Restaurant restaurant = new Restaurant(1L, restaurantUuid, "R", "A", "C", "9AM", 1L);

        MenuItem menuItem = new MenuItem(null, 1L, "", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));

        assertThrows(ValidationException.class, () -> useCase.execute(restaurantUuid, menuItem));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void testExecute_NullRestaurantIdValidation() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null, null));
        verify(menuItemGateway, never()).save(any());
    }
}

