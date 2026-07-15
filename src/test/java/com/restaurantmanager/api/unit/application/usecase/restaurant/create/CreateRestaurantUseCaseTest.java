package com.restaurantmanager.api.unit.application.usecase.restaurant.create;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.domain.exception.ValidationException;
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
    void testExecute_Success() {
        Restaurant restaurant = new Restaurant(null, "Resto", "Street", "Italian", "9AM", 1L);
        when(restaurantGateway.save(restaurant)).thenReturn(restaurant);

        Restaurant result = useCase.execute(restaurant);

        assertNotNull(result);
        verify(restaurantGateway).save(restaurant);
    }

    @Test
    void testExecute_InvalidRestaurant() {
        Restaurant restaurant = new Restaurant(null, "", "Street", "Italian", "9AM", 1L);
        assertThrows(ValidationException.class, () -> useCase.execute(restaurant));
        verify(restaurantGateway, never()).save(any());
    }

    @Test
    void testExecute_NullRestaurant() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(restaurantGateway, never()).save(any());
    }
}

