package com.restaurantmanager.api.unit.application.usecase.usertype.list;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.list.ListUserTypesUseCase;
import com.restaurantmanager.api.domain.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUserTypesUseCaseTest {

    @Mock
    private UserTypeGateway userTypeGateway;

    private ListUserTypesUseCase useCase;

    @BeforeEach
    void setUp() {
        useCase = new ListUserTypesUseCase(userTypeGateway);
    }

    @Test
    void testExecute_Success() {
        UserType userType1 = new UserType(1L, UUID.randomUUID(), "ADMIN", "Administrator");
        UserType userType2 = new UserType(2L, UUID.randomUUID(), "CUSTOMER", "Customer");
        when(userTypeGateway.findAll()).thenReturn(List.of(userType1, userType2));

        List<UserType> result = useCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userTypeGateway).findAll();
    }
}

