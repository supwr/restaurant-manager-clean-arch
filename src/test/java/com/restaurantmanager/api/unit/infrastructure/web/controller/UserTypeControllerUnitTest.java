package com.restaurantmanager.api.unit.infrastructure.web.controller;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.usertype.create.CreateUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.delete.DeleteUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.get.GetUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.list.ListUserTypesUseCase;
import com.restaurantmanager.api.application.usecase.usertype.update.UpdateUserTypeUseCase;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.web.controller.UserTypeController;
import com.restaurantmanager.api.infrastructure.web.exception.GlobalExceptionHandler;
import com.restaurantmanager.api.infrastructure.web.mapper.UserTypeMapper;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserTypeControllerUnitTest {

    @Mock private CreateUserTypeUseCase createUserTypeUseCase;
    @Mock private GetUserTypeUseCase getUserTypeUseCase;
    @Mock private ListUserTypesUseCase listUserTypesUseCase;
    @Mock private UpdateUserTypeUseCase updateUserTypeUseCase;
    @Mock private DeleteUserTypeUseCase deleteUserTypeUseCase;
    @Mock private UserTypeMapper userTypeMapper;
    @Mock private UserTypeGateway userTypeGateway;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserTypeController(
            createUserTypeUseCase, getUserTypeUseCase, listUserTypesUseCase, updateUserTypeUseCase, deleteUserTypeUseCase, userTypeMapper, userTypeGateway
        )).setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void testCreateUserType_Success() throws Exception {
        UserType domain = new UserType(1L, UUID.randomUUID(), "ADMIN");
        UserTypeResponse response = new UserTypeResponse(); response.setUuid(domain.getUuid()); response.setName("ADMIN");
        when(userTypeMapper.map(any(UserTypeRequest.class))).thenReturn(new UserType(null, "ADMIN"));
        when(createUserTypeUseCase.execute(any(UserType.class))).thenReturn(domain);
        when(userTypeMapper.map(domain)).thenReturn(response);

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"ADMIN\"}"))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("ADMIN"));
    }

    @Test
    void testListUserTypes_Success() throws Exception {
        UserType domain = new UserType(1L, UUID.randomUUID(), "ADMIN");
        UserTypeResponse response = new UserTypeResponse(); response.setUuid(domain.getUuid()); response.setName("ADMIN");
        when(listUserTypesUseCase.execute()).thenReturn(List.of(domain));
        when(userTypeMapper.map(domain)).thenReturn(response);

        mockMvc.perform(get("/api/v1/user-types"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetUserTypeByUuid_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        UserType domain = new UserType(1L, uuid, "ADMIN");
        UserTypeResponse response = new UserTypeResponse(); response.setUuid(uuid); response.setName("ADMIN");
        when(userTypeGateway.findByUuid(uuid)).thenReturn(Optional.of(domain));
        when(getUserTypeUseCase.execute(1L)).thenReturn(domain);
        when(userTypeMapper.map(domain)).thenReturn(response);

        mockMvc.perform(get("/api/v1/user-types/{uuid}", uuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("ADMIN"));
    }
}

