package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.UpdateUserRequest;
import com.restaurantmanager.api.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateUserRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new CreateUserRequest();
        validRequest.setName("Test User");
        validRequest.setEmail("testuser@example.com");
        validRequest.setLogin("testuser");
        UserType userType = new UserType();
        userType.setName("OWNER");
        validRequest.setType(userType);
    }

    @Test
    void testCreateUser_Success() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Test User")))
                .andExpect(jsonPath("$.email", equalTo("testuser@example.com")))
                .andExpect(jsonPath("$.login", equalTo("testuser")))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testListUsers_Success() throws Exception {
        // Create a user first
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

        // List users
        mockMvc.perform(get("/api/v1/users")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists());
    }

    @Test
    void testGetUserById_Success() throws Exception {
        // Create a user first
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Get user by uuid
        mockMvc.perform(get("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Test User")))
                .andExpect(jsonPath("$.uuid", equalTo(uuid)))
                .andExpect(jsonPath("$.email", equalTo("testuser@example.com")));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        // Create a user first
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Update user
        UpdateUserRequest updateRequest = new UpdateUserRequest();
        updateRequest.setName("Updated User");
        updateRequest.setEmail("updated@example.com");

        mockMvc.perform(put("/api/v1/users/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated User")))
                .andExpect(jsonPath("$.email", equalTo("updated@example.com")));
    }

    @Test
    void testDeleteUser_Success() throws Exception {
        // Create a user first
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Delete user
        mockMvc.perform(delete("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_InvalidData() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setName(""); // Invalid: empty name

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNonExistentUser() throws Exception {
        String fakeUuid = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(get("/api/v1/users/{uuid}", fakeUuid))
                .andExpect(status().isNotFound());
    }

    private String extractUuidFromJson(String json) {
        Pattern pattern = Pattern.compile("\"uuid\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}

