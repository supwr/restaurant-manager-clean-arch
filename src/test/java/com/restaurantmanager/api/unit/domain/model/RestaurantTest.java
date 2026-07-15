package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.Restaurant;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RestaurantTest {

    @Test
    void testCreateRestaurant_Success() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "123 Main St", "Italian", "9AM-10PM", 1L);

        assertNotNull(restaurant);
        assertEquals(1L, restaurant.getId());
        assertEquals("Pizzeria", restaurant.getName());
        assertEquals("123 Main St", restaurant.getAddress());
        assertEquals("Italian", restaurant.getCuisineType());
        assertEquals("9AM-10PM", restaurant.getOpeningHours());
        assertEquals(1L, restaurant.getOwnerUserId());
    }

    @Test
    void testCreateRestaurant_WithUuid() {
        UUID uuid = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(1L, uuid, "Pizzeria", "123 Main St", "Italian", "9AM-10PM", 1L);

        assertNotNull(restaurant);
        assertEquals(uuid, restaurant.getUuid());
        assertEquals(1L, restaurant.getId());
    }

    @Test
    void testRestaurantValidate_Success() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "123 Main St", "Italian", "9AM-10PM", 1L);

        assertDoesNotThrow(restaurant::validate);
    }

    @Test
    void testRestaurantValidate_BlankName() {
        Restaurant restaurant = new Restaurant(1L, "   ", "123 Main St", "Italian", "9AM-10PM", 1L);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testRestaurantValidate_NullName() {
        Restaurant restaurant = new Restaurant(1L, null, "123 Main St", "Italian", "9AM-10PM", 1L);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testRestaurantValidate_BlankAddress() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "   ", "Italian", "9AM-10PM", 1L);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("address", exception.getField());
    }

    @Test
    void testRestaurantValidate_NullCuisineType() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "123 Main St", null, "9AM-10PM", 1L);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("cuisineType", exception.getField());
    }

    @Test
    void testRestaurantValidate_NullOpeningHours() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "123 Main St", "Italian", null, 1L);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("openingHours", exception.getField());
    }

    @Test
    void testRestaurantValidate_NullOwnerUserId() {
        Restaurant restaurant = new Restaurant(1L, "Pizzeria", "123 Main St", "Italian", "9AM-10PM", null);

        ValidationException exception = assertThrows(ValidationException.class, restaurant::validate);
        assertEquals("ownerUserId", exception.getField());
    }
}

