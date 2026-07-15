package com.restaurantmanager.api.unit.application.usecase.user.update;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserByUuidUseCase;
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
class UpdateUserByUuidUseCaseTest {

    @Mock
    private UserGateway userGateway;

    private UpdateUserByUuidUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserByUuidUseCase(userGateway);
    }

    @Test
    void testUpdateUser_SuccessWithAllFields() {
        UUID uuid = UUID.randomUUID();
        long userId = 1L;
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, "New Name", "new@example.com", "newlogin", true, null, null, null, null);
        User updated = Owner.create(userId, uuid, "New Name", "new@example.com", "newlogin", true, null, null, now, now);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.empty());
        when(userGateway.findByLoginIgnoreCase("newlogin")).thenReturn(Optional.empty());
        when(userGateway.save(any(User.class))).thenReturn(updated);

        User result = useCase.execute(uuid, updateData);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());

        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        UUID uuid = UUID.randomUUID();
        User updateData = Owner.create(null, null, "New Name", "new@example.com", null, true, null, null, null, null);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(uuid, updateData));

        assertTrue(exception.getMessage().contains("User"));

        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        UUID uuid = UUID.randomUUID();
        long userId = 1L;
        Instant now = Instant.now();

        User existing = Owner.create(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, null, null, now, now);
        User otherUser = Owner.create(2L, UUID.randomUUID(), "Other", "new@example.com", "otherlogin", true, null, null, now, now);
        User updateData = Owner.create(null, null, null, "new@example.com", null, true, null, null, null, null);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.of(otherUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(uuid, updateData));

        assertEquals("email", exception.getField());

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUuid() {
        User updateData = Owner.create(null, null, "New Name", null, null, true, null, null, null, null);

        assertThrows(NullPointerException.class, () -> useCase.execute(null, updateData));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUser() {
        UUID uuid = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> useCase.execute(uuid, null));

        verify(userGateway, never()).save(any(User.class));
    }
}

