package com.restaurantmanager.api.unit.application.usecase.usertype.get;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.get.GetUserTypeUseCase;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
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
class GetUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private GetUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new GetUserTypeUseCase(userTypeGateway);
    }

    @Test
    void testExecute_Success() {
        UserType userType = new UserType(1L, UUID.randomUUID(), "ADMIN", "Administrator");
        when(userTypeGateway.findById(1L)).thenReturn(Optional.of(userType));

        UserType result = useCase.execute(1L);

        assertNotNull(result);
        assertEquals("ADMIN", result.getName());
        verify(userTypeGateway).findById(1L);
    }

    @Test
    void testExecute_NotFound() {
        when(userTypeGateway.findById(1L)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> useCase.execute(1L));

        assertTrue(exception.getMessage().contains("UserType"));
        verify(userTypeGateway).findById(1L);
    }

    @Test
    void testExecute_NullId() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(userTypeGateway, never()).findById(any());
    }
}

