package com.restaurantmanager.api.unit.application.usecase.user.update;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.UserType;
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

    @Mock
    private UserTypeGateway userTypeGateway;

    private UpdateUserUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserUseCase(userGateway, userTypeGateway);
    }

    @Test
    void testUpdateUser_SuccessWithAllFields() {
        UUID uuid = UUID.randomUUID();
        UUID userTypeUuid = UUID.randomUUID();
        long userId = 1L;
        Instant now = Instant.now();

        UserType userType = new UserType(1L, userTypeUuid, "OWNER");
        User existing = new User(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, userType, now, now);
        User updateData = new User(null, null, "New Name", "new@example.com", "newlogin", true, userType, null, null);
        User updated = new User(userId, uuid, "New Name", "new@example.com", "newlogin", true, userType, now, now);

        when(userTypeGateway.findByUuid(userTypeUuid)).thenReturn(Optional.of(userType));
        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.empty());
        when(userGateway.findByLoginIgnoreCase("newlogin")).thenReturn(Optional.empty());
        when(userGateway.save(any(User.class))).thenReturn(updated);

        User result = useCase.execute(uuid, updateData, userTypeUuid);

        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());

        verify(userTypeGateway).findByUuid(userTypeUuid);
        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        UUID uuid = UUID.randomUUID();
        User updateData = new User(null, null, "New Name", "new@example.com", null, true, null, null, null);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(uuid, updateData, null));

        assertTrue(exception.getMessage().contains("User"));

        verify(userGateway, times(1)).findByUuid(uuid);
        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_EmailAlreadyExists() {
        UUID uuid = UUID.randomUUID();
        long userId = 1L;
        Instant now = Instant.now();

        UserType userType = new UserType(1L, UUID.randomUUID(), "OWNER");
        User existing = new User(userId, uuid, "Old Name", "old@example.com", "oldlogin", true, userType, now, now);
        User otherUser = new User(2L, UUID.randomUUID(), "Other", "new@example.com", "otherlogin", true, userType, now, now);
        User updateData = new User(null, null, null, "new@example.com", null, true, null, null, null);

        when(userGateway.findByUuid(uuid)).thenReturn(Optional.of(existing));
        when(userGateway.findByEmailIgnoreCase("new@example.com")).thenReturn(Optional.of(otherUser));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(uuid, updateData, null));

        assertEquals("email", exception.getField());

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUuid() {
        User updateData = new User(null, null, "New Name", null, null, true, null, null, null);

        assertThrows(NullPointerException.class, () -> useCase.execute(null, updateData, null));

        verify(userGateway, never()).save(any(User.class));
    }

    @Test
    void testUpdateUser_NullUser() {
        UUID uuid = UUID.randomUUID();

        assertThrows(NullPointerException.class, () -> useCase.execute(uuid, null, null));

        verify(userGateway, never()).save(any(User.class));
    }
}

