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
        MenuItem menuItem = new MenuItem(null, 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.existsById(1L)).thenReturn(true);
        when(menuItemGateway.save(menuItem)).thenReturn(menuItem);

        MenuItem result = useCase.execute(menuItem);

        assertNotNull(result);
        verify(menuItemGateway).save(menuItem);
    }

    @Test
    void testExecute_RestaurantNotFound() {
        MenuItem menuItem = new MenuItem(null, 1L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.existsById(1L)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(menuItem));

        assertTrue(exception.getMessage().contains("Restaurant"));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void testExecute_InvalidMenuItem() {
        MenuItem menuItem = new MenuItem(null, 1L, "", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.existsById(1L)).thenReturn(true);

        assertThrows(ValidationException.class, () -> useCase.execute(menuItem));
        verify(menuItemGateway, never()).save(any());
    }

    @Test
    void testExecute_NullRestaurantIdValidation() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(menuItemGateway, never()).save(any());
    }
}

