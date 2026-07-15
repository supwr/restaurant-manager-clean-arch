package com.restaurantmanager.api.unit.application.usecase.usertype.update;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.update.UpdateUserTypeUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private UpdateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new UpdateUserTypeUseCase(userTypeGateway);
    }

    @Test
    void testUpdateUserType_Success() {
        long userTypeId = 1L;
        UUID uuid = UUID.randomUUID();

        UserType existing = new UserType(userTypeId, uuid, "ADMIN", "Administrator");
        UserType updateData = new UserType(userTypeId, "MANAGER", "Manager");
        UserType updated = new UserType(userTypeId, uuid, "MANAGER", "Manager");

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existing));
        when(userTypeGateway.existsByName("MANAGER")).thenReturn(false);
        when(userTypeGateway.save(any(UserType.class))).thenReturn(updated);

        UserType result = useCase.execute(userTypeId, updateData);

        assertNotNull(result);
        assertEquals("MANAGER", result.getName());

        verify(userTypeGateway, times(1)).findById(userTypeId);
        verify(userTypeGateway, times(1)).save(any(UserType.class));
    }

    @Test
    void testUpdateUserType_NotFound() {
        long userTypeId = 999L;
        UserType updateData = new UserType(userTypeId, "MANAGER", "Manager");

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(userTypeId, updateData));

        assertTrue(exception.getMessage().contains("UserType"));

        verify(userTypeGateway, times(1)).findById(userTypeId);
        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void testUpdateUserType_NameAlreadyExists() {
        long userTypeId = 1L;
        UUID uuid = UUID.randomUUID();

        UserType existing = new UserType(userTypeId, uuid, "ADMIN", "Administrator");
        UserType updateData = new UserType(userTypeId, "MANAGER", "Manager");

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existing));
        when(userTypeGateway.existsByName("MANAGER")).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userTypeId, updateData));

        assertEquals("name", exception.getField());

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void testUpdateUserType_ValidationError_BlankName() {
        long userTypeId = 1L;
        UUID uuid = UUID.randomUUID();

        UserType existing = new UserType(userTypeId, uuid, "ADMIN", "Administrator");
        UserType updateData = new UserType(userTypeId, "   ", "Manager");

        when(userTypeGateway.findById(userTypeId)).thenReturn(Optional.of(existing));

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userTypeId, updateData));

        assertEquals("name", exception.getField());

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void testUpdateUserType_NullId() {
        UserType updateData = new UserType(1L, "MANAGER", "Manager");

        assertThrows(NullPointerException.class, () -> useCase.execute(null, updateData));

        verify(userTypeGateway, never()).save(any(UserType.class));
    }

    @Test
    void testUpdateUserType_NullUserType() {
        assertThrows(NullPointerException.class, () -> useCase.execute(1L, null));

        verify(userTypeGateway, never()).save(any(UserType.class));
    }
}

