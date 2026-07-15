package com.restaurantmanager.api.unit.application.usecase.menuitem.list;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.menuitem.list.ListMenuItemsUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListMenuItemsUseCaseTest {

    @Mock
    private MenuItemGateway menuItemGateway;

    @Mock
    private RestaurantGateway restaurantGateway;

    private ListMenuItemsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListMenuItemsUseCase(menuItemGateway, restaurantGateway);
    }

    @Test
    void testListMenuItems_Success() {
        long restaurantId = 1L;
        Pagination pagination = new Pagination(0, 20, "id");

        List<MenuItem> items = new ArrayList<>();
        items.add(new MenuItem(1L, UUID.randomUUID(), restaurantId, "Pizza", "Delicious", new BigDecimal("10.50"), false, "/pizza"));
        items.add(new MenuItem(2L, UUID.randomUUID(), restaurantId, "Burger", "Tasty", new BigDecimal("12.00"), true, "/burger"));

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(items);

        PageResult<MenuItem> result = useCase.execute(restaurantId, pagination);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(0, result.getPageNumber());
        assertEquals(20, result.getPageSize());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());

        verify(restaurantGateway, times(1)).existsById(restaurantId);
        verify(menuItemGateway, times(1)).findByRestaurantId(restaurantId);
    }

    @Test
    void testListMenuItems_Paginated() {
        long restaurantId = 1L;
        Pagination pagination = new Pagination(1, 2, "id");

        List<MenuItem> items = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            items.add(new MenuItem((long) i, UUID.randomUUID(), restaurantId, "Item" + i, "Description", new BigDecimal("10.00"), false, "/path"));
        }

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(items);

        PageResult<MenuItem> result = useCase.execute(restaurantId, pagination);

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(1, result.getPageNumber());
        assertEquals(2, result.getPageSize());
        assertEquals(5L, result.getTotalElements());
        assertEquals(3, result.getTotalPages());
    }

    @Test
    void testListMenuItems_Empty() {
        long restaurantId = 1L;
        Pagination pagination = new Pagination(0, 20, "id");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);
        when(menuItemGateway.findByRestaurantId(restaurantId)).thenReturn(new ArrayList<>());

        PageResult<MenuItem> result = useCase.execute(restaurantId, pagination);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
        assertEquals(0, result.getTotalElements());
        assertEquals(0, result.getTotalPages());
    }

    @Test
    void testListMenuItems_RestaurantNotFound() {
        long restaurantId = 999L;
        Pagination pagination = new Pagination(0, 20, "id");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(restaurantId, pagination));

        assertTrue(exception.getMessage().contains("Restaurant"));

        verify(menuItemGateway, never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testListMenuItems_InvalidPageSize() {
        long restaurantId = 1L;
        Pagination pagination = new Pagination(0, 0, "id");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, pagination));

        assertEquals("size", exception.getField());

        verify(menuItemGateway, never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testListMenuItems_NegativePageSize() {
        long restaurantId = 1L;
        Pagination pagination = new Pagination(0, -5, "id");

        when(restaurantGateway.existsById(restaurantId)).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(restaurantId, pagination));

        assertEquals("size", exception.getField());

        verify(menuItemGateway, never()).findByRestaurantId(restaurantId);
    }

    @Test
    void testListMenuItems_NullRestaurantId() {
        Pagination pagination = new Pagination(0, 20, "id");

        assertThrows(NullPointerException.class, () -> useCase.execute(null, pagination));

        verify(restaurantGateway, never()).existsById(any());
        verify(menuItemGateway, never()).findByRestaurantId(any());
    }

    @Test
    void testListMenuItems_NullPagination() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null));

        verify(restaurantGateway, never()).existsById(any());
        verify(menuItemGateway, never()).findByRestaurantId(any());
    }
}

