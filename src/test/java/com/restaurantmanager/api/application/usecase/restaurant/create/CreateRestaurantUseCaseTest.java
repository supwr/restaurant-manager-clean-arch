package com.restaurantmanager.api.application.usecase.restaurant.create;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateRestaurantUseCaseTest {

    @Mock
    private RestaurantGateway restaurantGateway;

    private CreateRestaurantUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateRestaurantUseCase(restaurantGateway);
    }

    @Test
    void testCreateRestaurantSuccess() {
        Restaurant restaurantToCreate = new Restaurant(null, "Test Restaurant", "123 Main St", "Italian", "9AM-10PM", 1L);

        Restaurant savedRestaurant = new Restaurant(1L, "Test Restaurant", "123 Main St", "Italian", "9AM-10PM", 1L);

        when(restaurantGateway.save(any(Restaurant.class))).thenReturn(savedRestaurant);

        Restaurant result = useCase.execute(restaurantToCreate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Test Restaurant", result.getName());
        assertEquals("123 Main St", result.getAddress());
        assertEquals("Italian", result.getCuisineType());

        verify(restaurantGateway, times(1)).save(any(Restaurant.class));
    }

    @Test
    void testCreateRestaurantNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(restaurantGateway, never()).save(any(Restaurant.class));
    }
}

