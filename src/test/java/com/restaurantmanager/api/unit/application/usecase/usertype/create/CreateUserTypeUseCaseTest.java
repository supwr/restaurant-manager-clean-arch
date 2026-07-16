package com.restaurantmanager.api.unit.application.usecase.usertype.create;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.create.CreateUserTypeUseCase;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserTypeUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private CreateUserTypeUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new CreateUserTypeUseCase(userTypeGateway);
    }

    @Test
    void testExecute_Success() {
        UserType userType = new UserType(null, "ADMIN");
        when(userTypeGateway.existsByName("ADMIN")).thenReturn(false);
        when(userTypeGateway.save(userType)).thenReturn(userType);

        UserType result = useCase.execute(userType);

        assertNotNull(result);
        verify(userTypeGateway).save(userType);
    }

    @Test
    void testExecute_DuplicateName() {
        UserType userType = new UserType(null, "ADMIN");
        when(userTypeGateway.existsByName("ADMIN")).thenReturn(true);

        ValidationException exception = assertThrows(ValidationException.class, () -> useCase.execute(userType));

        assertEquals("name", exception.getField());
        verify(userTypeGateway, never()).save(any());
    }

    @Test
    void testExecute_InvalidName() {
        UserType userType = new UserType(null, "");
        assertThrows(ValidationException.class, () -> useCase.execute(userType));
        verify(userTypeGateway, never()).save(any());
    }

    @Test
    void testExecute_NullUserType() {
        assertThrows(NullPointerException.class, () -> useCase.execute(null));
        verify(userTypeGateway, never()).save(any());
    }
}

