package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.UserTypeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.openapitools.jackson.nullable.JsonNullableModule;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class UserTypeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper.registerModule(new JsonNullableModule());
    }

    @Test
    void testCreateUserType_Success() throws Exception {
        UserTypeRequest request = new UserTypeRequest();
        request.setName("Admin");
        request.observation("Administrator role");

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Admin")))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testListUserTypes_Success() throws Exception {
        // Create a user type first
        UserTypeRequest request = new UserTypeRequest();
        request.setName("Manager");
        request.observation("Manager role");

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // List user types
        mockMvc.perform(get("/api/v1/user-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists());
    }

    @Test
    void testGetUserTypeByUuid_Success() throws Exception {
        // Create a user type first
        UserTypeRequest request = new UserTypeRequest();
        request.setName("User");
        request.observation("Regular user role");

        MvcResult createResult = mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Get user type by uuid
        mockMvc.perform(get("/api/v1/user-types/{userTypeUuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("User")))
                .andExpect(jsonPath("$.uuid", equalTo(uuid)));
    }

    @Test
    void testUpdateUserType_Success() throws Exception {
        // Create a user type first
        UserTypeRequest request = new UserTypeRequest();
        request.setName("Guest");
        request.observation("Guest role");

        MvcResult createResult = mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Update user type
        UserTypeRequest updateRequest = new UserTypeRequest();
        updateRequest.setName("Premium Guest");
        updateRequest.observation("Premium guest role with special privileges");

        mockMvc.perform(put("/api/v1/user-types/{userTypeUuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Premium Guest")))
                .andExpect(jsonPath("$.observation").doesNotExist());
    }

    @Test
    void testDeleteUserType_Success() throws Exception {
        // Create a user type first
        UserTypeRequest request = new UserTypeRequest();
        request.setName("Temp User");
        request.observation("Temporary user role");

        MvcResult createResult = mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Delete user type
        mockMvc.perform(delete("/api/v1/user-types/{userTypeUuid}", uuid))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/user-types/{userTypeUuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUserType_InvalidData() throws Exception {
        UserTypeRequest invalidRequest = new UserTypeRequest();
        invalidRequest.setName(""); // Invalid: empty name

        mockMvc.perform(post("/api/v1/user-types")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNonExistentUserType() throws Exception {
        String fakeUuid = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(get("/api/v1/user-types/{userTypeUuid}", fakeUuid))
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

