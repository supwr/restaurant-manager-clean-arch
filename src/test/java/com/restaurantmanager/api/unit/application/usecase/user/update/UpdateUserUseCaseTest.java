package com.restaurantmanager.api.unit.application.usecase.user.update;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
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
class UpdateUserUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private UpdateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserUseCase(userGateway);
    }

    @Test
    void testUpdateUser_SuccessWithAllFields() {
        long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, "New Name", "new@example.com", "newlogin", true, null, null, null, null);
        User updated = Owner.create(userId, uuid, "New Name", "new@example.com", "newlogin", true, null, null, now, now);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.empty());
        when(userGateway.findByLoginIgnoreCase("newlogin")).thenReturn(Optional.empty());
        when(userGateway.save(any(User.class))).thenReturn(updated);

        User result = useCase.execute(userId, updateData);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());
        assertEquals("newlogin", result.getLogin());

        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).findByEmailIgnoreCase("new@example.com");
        verify(userGateway, times(1)).findByLoginIgnoreCase("newlogin");
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_PartialUpdate() {
        long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, "New Name", null, null, true, null, null, null, null);
        User updated = Owner.create(userId, uuid, "New Name", "old@example.com", "oldlogin", true, null, null, now, now);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existing));
        when(userGateway.save(any(User.class))).thenReturn(updated);

        User result = useCase.execute(userId, updateData);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("old@example.com", result.getEmail());

        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        long userId = 999L;
        User updateData = Owner.create(null, null, "New Name", "new@example.com", "newlogin", true, null, null, null, null);

        when(userGateway.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(userId, updateData));

        assertTrue(exception.getMessage().contains("User"));

        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User otherUser = Owner.create(2L, UUID.randomUUID(), "Other", "new@example.com", "otherlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, "New Name", "new@example.com", null, true, null, null, null, null);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.of(otherUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userId, updateData));

        assertEquals("email", exception.getField());

        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, times(1)).findByEmailIgnoreCase("new@example.com");
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_LoginAlreadyExists() {
        long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User otherUser = Owner.create(2L, UUID.randomUUID(), "Other", "other@example.com", "newlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, null, null, "newlogin", true, null, null, null, null);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existing));
        when(userGateway.findByLoginIgnoreCase("newlogin")).thenReturn(Optional.of(otherUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userId, updateData));

        assertEquals("login", exception.getField());

        verify(userGateway, times(1)).findById(userId);
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_BlankName() {
        long userId = 1L;
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, "   ", null, null, true, null, null, null, null);

        when(userGateway.findById(userId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userId, updateData));

        assertEquals("name", exception.getField());

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullId() {
        User updateData = Owner.create(null, null, "New Name", null, null, true, null, null, null, null);

        assertThrows(NullPointerException.class, () -> useCase.execute(null, updateData));

        verify(userGateway, never()).findById(any());
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUser() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null));

        verify(userGateway, never()).save(any(User.class));
    }
}

