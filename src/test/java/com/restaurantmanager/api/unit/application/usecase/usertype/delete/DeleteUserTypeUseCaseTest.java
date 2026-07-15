package com.restaurantmanager.api.unit.application.usecase.usertype.delete;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.delete.DeleteUserTypeUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private DeleteUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new DeleteUserTypeUseCase(userTypeGateway);
    }

    @Test
    void testDeleteUserType_Success() {
        long userTypeId = 1L;

        when(userTypeGateway.existsById(userTypeId)).thenReturn(true);

        assertDoesNotThrow(() -> useCase.execute(userTypeId));

        verify(userTypeGateway, times(1)).existsById(userTypeId);
        verify(userTypeGateway, times(1)).deleteById(userTypeId);
    }

    @Test
    void testDeleteUserType_NotFound() {
        long userTypeId = 999L;

        when(userTypeGateway.existsById(userTypeId)).thenReturn(false);

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(userTypeId));

        assertTrue(exception.getMessage().contains("UserType"));

        verify(userTypeGateway, times(1)).existsById(userTypeId);
        verify(userTypeGateway, never()).deleteById(anyLong());
    }

    @Test
    void testDeleteUserType_NullId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));

        verify(userTypeGateway, never()).existsById(anyLong());
        verify(userTypeGateway, never()).deleteById(anyLong());
    }
}

