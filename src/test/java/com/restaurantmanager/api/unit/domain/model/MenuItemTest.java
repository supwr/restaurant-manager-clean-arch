package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.MenuItem;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class MenuItemTest {

    @Test
    void testCreateMenuItem_Success() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/pizza.jpg");

        assertNotNull(item);
        assertEquals(1L, item.getId());
        assertEquals(1L, item.getRestaurantId());
        assertEquals("Pizza", item.getName());
        assertEquals("Delicious pizza", item.getDescription());
        assertEquals(new BigDecimal("10.50"), item.getPrice());
        assertFalse(item.getLocalOnly());
        assertEquals("/pizza.jpg", item.getPhotoPath());
    }

    @Test
    void testCreateMenuItem_WithUuid() {
        UUID uuid = UUID.randomUUID();
        MenuItem item = new MenuItem(1L, uuid, 1L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), false, "/pizza.jpg");

        assertNotNull(item);
        assertEquals(uuid, item.getUuid());
        assertEquals(1L, item.getId());
    }

    @Test
    void testMenuItemValidate_Success() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Delicious pizza", new BigDecimal("10.50"), true, "/pizza.jpg");

        assertDoesNotThrow(item::validate);
    }

    @Test
    void testMenuItemValidate_NullRestaurantId() {
        MenuItem item = new MenuItem(1L, null, "Pizza", "Description", new BigDecimal("10.50"), true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("restaurantId", exception.getField());
    }

    @Test
    void testMenuItemValidate_BlankName() {
        MenuItem item = new MenuItem(1L, 1L, "   ", "Description", new BigDecimal("10.50"), true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testMenuItemValidate_NullName() {
        MenuItem item = new MenuItem(1L, 1L, null, "Description", new BigDecimal("10.50"), true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("name", exception.getField());
    }

    @Test
    void testMenuItemValidate_BlankDescription() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "   ", new BigDecimal("10.50"), true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("description", exception.getField());
    }

    @Test
    void testMenuItemValidate_NullPrice() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", null, true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("price", exception.getField());
    }

    @Test
    void testMenuItemValidate_ZeroPrice() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", BigDecimal.ZERO, true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("price", exception.getField());
    }

    @Test
    void testMenuItemValidate_NegativePrice() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", new BigDecimal("-10.50"), true, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("price", exception.getField());
    }

    @Test
    void testMenuItemValidate_NullLocalOnly() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", new BigDecimal("10.50"), null, "/path");

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("localOnly", exception.getField());
    }

    @Test
    void testMenuItemValidate_NullPhotoPath() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", new BigDecimal("10.50"), true, null);

        ValidationException exception = assertThrows(ValidationException.class, item::validate);
        assertEquals("photoPath", exception.getField());
    }

    @Test
    void testMenuItemValidate_EmptyPhotoPath() {
        MenuItem item = new MenuItem(1L, 1L, "Pizza", "Description", new BigDecimal("10.50"), true, "");

        assertDoesNotThrow(item::validate);
    }
}

