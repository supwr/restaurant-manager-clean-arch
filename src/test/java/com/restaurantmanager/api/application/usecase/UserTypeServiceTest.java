package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.application.usecase.usertype.UserTypeService;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

    @Mock
    private UserTypeGateway persistencePort;

    @InjectMocks
    private UserTypeService userTypeService;

    private UserType validUserType;

    @BeforeEach
    void setUp() {
        validUserType = UserType.builder()
            .name("Restaurant Owner")
            .observation("Owner of a restaurant")
            .build();
    }

    @Test
    void testCreateSuccess() {
        // Arrange
        when(persistencePort.existsByName(validUserType.getName())).thenReturn(false);
        when(persistencePort.save(any(UserType.class))).thenReturn(
            UserType.builder()
                .id(1L)
                .name(validUserType.getName())
                .observation(validUserType.getObservation())
                .build()
        );

        // Act
        UserType created = userTypeService.create(validUserType);

        // Assert
        assertNotNull(created);
        assertEquals(1L, created.getId());
        assertEquals("Restaurant Owner", created.getName());
        verify(persistencePort, times(1)).save(any(UserType.class));
    }

    @Test
    void testCreateWithDuplicateName() {
        // Arrange
        when(persistencePort.existsByName(validUserType.getName())).thenReturn(true);

        // Act & Assert
        assertThrows(ValidationException.class, () -> userTypeService.create(validUserType));
        verify(persistencePort, never()).save(any());
    }

    @Test
    void testCreateWithBlankName() {
        // Arrange
        UserType invalidUserType = UserType.builder()
            .name("  ")
            .observation("Test")
            .build();

        // Act & Assert
        assertThrows(ValidationException.class, () -> userTypeService.create(invalidUserType));
    }

    @Test
    void testGetByIdSuccess() {
        // Arrange
        UserType userType = UserType.builder()
            .id(1L)
            .name("Restaurant Owner")
            .observation("Test observation")
            .build();
        when(persistencePort.findById(1L)).thenReturn(Optional.of(userType));

        // Act
        UserType result = userTypeService.getById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Restaurant Owner", result.getName());
    }

    @Test
    void testGetByIdNotFound() {
        // Arrange
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userTypeService.getById(999L));
    }

    @Test
    void testListAllSuccess() {
        // Arrange
        List<UserType> userTypes = Arrays.asList(
            UserType.builder().id(1L).name("Owner").build(),
            UserType.builder().id(2L).name("Customer").build()
        );
        when(persistencePort.findAll()).thenReturn(userTypes);

        // Act
        List<UserType> result = userTypeService.listAll();

        // Assert
        assertEquals(2, result.size());
        verify(persistencePort, times(1)).findAll();
    }

    @Test
    void testUpdateSuccess() {
        // Arrange
        UserType existing = UserType.builder()
            .id(1L)
            .name("Old Name")
            .observation("Old observation")
            .build();
        UserType updated = UserType.builder()
            .name("New Name")
            .observation("New observation")
            .build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.existsByName("New Name")).thenReturn(false);
        when(persistencePort.save(any(UserType.class))).thenReturn(
            UserType.builder()
                .id(1L)
                .name("New Name")
                .observation("New observation")
                .build()
        );

        // Act
        UserType result = userTypeService.update(1L, updated);

        // Assert
        assertEquals("New Name", result.getName());
        verify(persistencePort, times(1)).save(any(UserType.class));
    }

    @Test
    void testUpdateNotFound() {
        // Arrange
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userTypeService.update(999L, validUserType));
    }

    @Test
    void testUpdateWithDuplicateName() {
        // Arrange
        UserType existing = UserType.builder()
            .id(1L)
            .name("Owner")
            .build();
        UserType updated = UserType.builder()
            .name("Customer")
            .build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.existsByName("Customer")).thenReturn(true);

        // Act & Assert
        assertThrows(ValidationException.class, () -> userTypeService.update(1L, updated));
    }

    @Test
    void testDeleteSuccess() {
        // Arrange
        when(persistencePort.existsById(1L)).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> userTypeService.delete(1L));

        // Assert
        verify(persistencePort, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteNotFound() {
        // Arrange
        when(persistencePort.existsById(999L)).thenReturn(false);

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> userTypeService.delete(999L));
    }
}

