package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.application.usecase.menuitem.MenuItemService;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuItemServiceTest {

    @Mock
    private MenuItemGateway menuItemPort;

    @Mock
    private RestaurantGateway restaurantPort;

    @InjectMocks
    private MenuItemService service;

    private MenuItem valid;

    @BeforeEach
    void setUp() {
        valid = MenuItem.builder()
            .restaurantId(1L)
            .name("Margherita")
            .description("Classic")
            .price(new BigDecimal("10.00"))
            .localOnly(false)
            .photoPath("/img.jpg")
            .build();
    }

    @Test
    void createSuccess() {
        when(restaurantPort.existsById(1L)).thenReturn(true);
        when(menuItemPort.save(any(MenuItem.class))).thenReturn(MenuItem.builder().id(1L).name(valid.getName()).build());

        var created = service.create(valid);
        assertNotNull(created);
        assertEquals(1L, created.getId());
    }

    @Test
    void createRestaurantNotFound() {
        when(restaurantPort.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.create(valid));
    }

    @Test
    void getByIdSuccess() {
        when(menuItemPort.findById(1L)).thenReturn(Optional.of(MenuItem.builder().id(1L).name("X").build()));
        var item = service.getById(1L);
        assertEquals(1L, item.getId());
    }

    @Test
    void getByIdNotFound() {
        when(menuItemPort.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void listByRestaurantSuccess() {
        when(restaurantPort.existsById(1L)).thenReturn(true);
        List<MenuItem> items = Arrays.asList(MenuItem.builder().id(1L).name("A").build());
        when(menuItemPort.findByRestaurantId(1L)).thenReturn(items);

        var result = service.listByRestaurant(1L);
        assertEquals(1, result.size());
    }

    @Test
    void listByRestaurantNotFound() {
        when(restaurantPort.existsById(1L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.listByRestaurant(1L));
    }

    @Test
    void updateSuccess() {
        MenuItem existing = MenuItem.builder().id(1L).restaurantId(1L).name("Old").description("d").price(new BigDecimal("5.00")).localOnly(false).photoPath("").build();
        MenuItem updated = MenuItem.builder().restaurantId(1L).name("New").description("d").price(new BigDecimal("6.00")).localOnly(false).photoPath("").build();

        when(menuItemPort.findById(1L)).thenReturn(Optional.of(existing));
        when(restaurantPort.existsById(1L)).thenReturn(true);
        when(menuItemPort.save(any(MenuItem.class))).thenReturn(MenuItem.builder().id(1L).name("New").build());

        var r = service.update(1L, updated);
        assertEquals("New", r.getName());
    }

    @Test
    void updateNotFound() {
        when(menuItemPort.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(999L, valid));
    }

    @Test
    void deleteSuccess() {
        when(menuItemPort.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> service.delete(1L));
        verify(menuItemPort, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFound() {
        when(menuItemPort.existsById(999L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.delete(999L));
    }
}

