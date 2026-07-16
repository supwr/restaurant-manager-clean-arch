package com.restaurantmanager.api.unit.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.application.usecase.user.create.CreateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserUseCase;
import com.restaurantmanager.api.application.usecase.user.get.GetUserUseCase;
import com.restaurantmanager.api.application.usecase.user.list.ListUserCase;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserUseCase;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.web.controller.UserController;
import com.restaurantmanager.api.infrastructure.web.exception.GlobalExceptionHandler;
import com.restaurantmanager.api.infrastructure.web.mapper.UserMapper;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerUnitTest {

    @Mock private CreateUserUseCase createUserUseCase;
    @Mock private GetUserUseCase getUserByUuidUseCase;
    @Mock private ListUserCase listUserCase;
    @Mock private UpdateUserUseCase updateUserByUuidUseCase;
    @Mock private DeleteUserUseCase deleteUserByUuidUseCase;
    @Mock private UserMapper userMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new UserController(
            createUserUseCase, getUserByUuidUseCase, listUserCase, updateUserByUuidUseCase, deleteUserByUuidUseCase, userMapper
        )).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateUser_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        com.restaurantmanager.api.domain.model.UserType domainType = new com.restaurantmanager.api.domain.model.UserType(1L, UUID.randomUUID(), "OWNER");
        User user = new User(1L, uuid, "John Doe", "john@example.com", "john", true, domainType, Instant.now(), Instant.now());
        UserResponse response = new UserResponse();
        response.setUuid(uuid);
        response.setName("John Doe");
        response.setEmail("john@example.com");
        UserType type = new UserType();
        UUID typeUuid = UUID.randomUUID();
        type.setUuid(typeUuid);
        type.setName("OWNER");
        response.setType(type);

        when(createUserUseCase.execute(any(User.class))).thenReturn(user);
        when(userMapper.map(user)).thenReturn(response);

        CreateUserRequest request = new CreateUserRequest();
        request.setName("John Doe");
        request.setEmail("john@example.com");
        request.setLogin("john");
        UserType requestType = new UserType();
        requestType.setName("OWNER");
        request.setType(requestType);

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("John Doe"))
            .andExpect(jsonPath("$.uuid").exists());

        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(createUserUseCase).execute(captor.capture());
        assertEquals("John Doe", captor.getValue().getName());
    }

    @Test
    void testGetUserByUuid_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        com.restaurantmanager.api.domain.model.UserType domainType = new com.restaurantmanager.api.domain.model.UserType(1L, UUID.randomUUID(), "OWNER");
        User user = new User(1L, uuid, "John Doe", "john@example.com", "john", true, domainType, Instant.now(), Instant.now());
        UserResponse response = new UserResponse();
        response.setUuid(uuid);
        response.setName("John Doe");
        response.setEmail("john@example.com");
        UserType type = new UserType();
        type.setUuid(UUID.randomUUID());
        type.setName("OWNER");
        response.setType(type);

        when(getUserByUuidUseCase.execute(uuid)).thenReturn(user);
        when(userMapper.map(user)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users/{uuid}", uuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testListUsers_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        com.restaurantmanager.api.domain.model.UserType domainType = new com.restaurantmanager.api.domain.model.UserType(1L, UUID.randomUUID(), "OWNER");
        User user = new User(1L, uuid, "John Doe", "john@example.com", "john", true, domainType, Instant.now(), Instant.now());
        UserResponse response = new UserResponse();
        response.setUuid(uuid);
        response.setName("John Doe");
        UserType type = new UserType();
        type.setUuid(UUID.randomUUID());
        type.setName("OWNER");
        response.setType(type);

        when(listUserCase.execute(any(Pagination.class))).thenReturn(new PageResult<>(List.of(user), 0, 20, 1L, 1));
        when(userMapper.map(user)).thenReturn(response);

        mockMvc.perform(get("/api/v1/users"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        UUID uuid = UUID.randomUUID();

        mockMvc.perform(delete("/api/v1/users/{uuid}", uuid))
            .andExpect(status().isNoContent());

        verify(deleteUserByUuidUseCase).execute(uuid);
    }
}

