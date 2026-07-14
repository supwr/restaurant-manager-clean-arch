package com.restaurantmanager.api.application.usecase.user.create;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private CreateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserUseCase(userGateway);
    }

    @Test
    void testCreateUserSuccess() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User userToCreate = Owner.create(null, null, "John Doe", "john@example.com", "john.doe", true, null, null, null, null);

        User savedUser = Owner.create(1L, uuid, "John Doe", "john@example.com", "john.doe", true, null, null, now, now);

        when(userGateway.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.empty());
        when(userGateway.findByLoginIgnoreCase("john.doe")).thenReturn(Optional.empty());
        when(userGateway.save(any(User.class))).thenReturn(savedUser);

        User result = useCase.execute(userToCreate);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals("john.doe", result.getLogin());

        verify(userGateway, times(1)).findByEmailIgnoreCase("john@example.com");
        verify(userGateway, times(1)).findByLoginIgnoreCase("john.doe");
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void testCreateUserEmailAlreadyExists() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User existingUser = Owner.create(1L, uuid, "Existing User", "john@example.com", "existing.user", true, null, null, now, now);
        User userToCreate = Owner.create(null, null, "John Doe", "john@example.com", "john.doe", true, null, null, null, null);

        when(userGateway.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.of(existingUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userToCreate));

        assertEquals("email", exception.getField());
        assertTrue(exception.getMessage().contains("Email already exists"));

        verify(userGateway, times(1)).findByEmailIgnoreCase("john@example.com");
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testCreateUserLoginAlreadyExists() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User existingUser = Owner.create(1L, uuid, "Existing User", "existing@example.com", "john.doe", true, null, null, now, now);
        User userToCreate = Owner.create(null, null, "John Doe", "john@example.com", "john.doe", true, null, null, null, null);

        when(userGateway.findByEmailIgnoreCase("john@example.com")).thenReturn(Optional.empty());
        when(userGateway.findByLoginIgnoreCase("john.doe")).thenReturn(Optional.of(existingUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userToCreate));

        assertEquals("login", exception.getField());
        assertTrue(exception.getMessage().contains("Login already exists"));

        verify(userGateway, times(1)).findByEmailIgnoreCase("john@example.com");
        verify(userGateway, times(1)).findByLoginIgnoreCase("john.doe");
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testCreateUserMissingName() {
        User userToCreate = Owner.create(null, null, null, "john@example.com", "john.doe", true, null, null, null, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userToCreate));

        assertEquals("name", exception.getField());
        assertTrue(exception.getMessage().contains("User name is required"));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testCreateUserMissingEmail() {
        User userToCreate = Owner.create(null, null, "John Doe", null, "john.doe", true, null, null, null, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userToCreate));

        assertEquals("email", exception.getField());
        assertTrue(exception.getMessage().contains("User email is required"));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testCreateUserMissingLogin() {
        User userToCreate = Owner.create(null, null, "John Doe", "john@example.com", null, true, null, null, null, null);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userToCreate));

        assertEquals("login", exception.getField());
        assertTrue(exception.getMessage().contains("User login is required"));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testCreateUserNull() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userGateway, never()).save(any(User.class));
    }
}

