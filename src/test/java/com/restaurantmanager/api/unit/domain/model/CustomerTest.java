package com.restaurantmanager.api.unit.domain.model;

import com.restaurantmanager.api.domain.model.Customer;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testCreateCustomer_Success() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        Customer customer = Customer.create(1L, uuid, "Jane Doe", "jane@example.com", "janedoe", true, "password123", null, now, now);

        assertNotNull(customer);
        assertEquals(1L, customer.getId());
        assertEquals(uuid, customer.getUuid());
        assertEquals("Jane Doe", customer.getName());
        assertEquals("jane@example.com", customer.getEmail());
        assertEquals("janedoe", customer.getLogin());
    }

    @Test
    void testCreateCustomer_WithoutId() {
        Customer customer = Customer.create(null, null, "Jane Doe", "jane@example.com", "janedoe", true, null, null, null, null);

        assertNotNull(customer);
        assertNull(customer.getId());
        assertNull(customer.getUuid());
    }

    @Test
    void testCreateCustomer_ExtendsUser() {
        Customer customer = Customer.create(1L, UUID.randomUUID(), "Jane", "jane@example.com", "jane", true, null, null, null, null);

        assertEquals("Customer", customer.getClass().getSimpleName());
    }
}

