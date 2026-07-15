package com.restaurantmanager.api.unit.application.usecase.restaurant.list;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListRestaurantsUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private ListRestaurantsUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListRestaurantsUseCase(restaurantGateway);
    }

    @Test
    void testExecute_Success() {
        Restaurant restaurant1 = new Restaurant(1L, UUID.randomUUID(), "R1", "A1", "Italian", "9AM", 1L);
        Restaurant restaurant2 = new Restaurant(2L, UUID.randomUUID(), "R2", "A2", "French", "10AM", 2L);
        when(restaurantGateway.findAll()).thenReturn(List.of(restaurant1, restaurant2));

        PageResult<Restaurant> result = useCase.execute(new Pagination(0, 20, "id"));

        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(2L, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        verify(restaurantGateway).findAll();
    }

    @Test
    void testExecute_InvalidPageSize() {
        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(new Pagination(0, 0, "id")));
        assertEquals("size", exception.getField());
        verify(restaurantGateway, never()).findAll();
    }

    @Test
    void testExecute_NullPagination() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(restaurantGateway, never()).findAll();
    }
}

