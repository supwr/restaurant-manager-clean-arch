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
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private CreateUserRequest validRequest;

    @BeforeEach
    void setUp() throws Exception {
        seedDefaultUserTypes();

        final String suffix = UUID.randomUUID().toString().substring(0, 8);
        validRequest = new CreateUserRequest();
        validRequest.setName("Test User " + suffix);
        validRequest.setEmail("testuser-" + suffix + "@example.com");
        validRequest.setLogin("testuser-" + suffix);
        // Prefer seeded user type if present, otherwise create it and use its uuid
        String typeUuid = null;
        try {
            typeUuid = jdbcTemplate.queryForObject("select uuid from user_types where name = ?", String.class, "OWNER");
        } catch (Exception ignored) {
        }
        if (typeUuid == null) {
            com.restaurantmanager.api.model.UserTypeRequest userTypeRequest = new com.restaurantmanager.api.model.UserTypeRequest();
            userTypeRequest.setName("OWNER");
            MvcResult createTypeResult = mockMvc.perform(post("/api/v1/user-types")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(userTypeRequest)))
                    .andExpect(status().isCreated())
                    .andReturn();

            typeUuid = extractUuidFromJson(createTypeResult.getResponse().getContentAsString());
        }
        com.restaurantmanager.api.model.UserTypeRef userType = new com.restaurantmanager.api.model.UserTypeRef();
        userType.setId(UUID.fromString(typeUuid));
        validRequest.setType(userType);
    }

    @Test
    void testCreateUser_Success() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(validRequest.getName())))
                .andExpect(jsonPath("$.email", equalTo(validRequest.getEmail())))
                .andExpect(jsonPath("$.login", equalTo(validRequest.getLogin())))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.type.uuid").exists())
                .andExpect(jsonPath("$.type.name", equalTo("OWNER")))
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testListUsers_Success() throws Exception {
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated());

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
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        mockMvc.perform(get("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(validRequest.getName())))
                .andExpect(jsonPath("$.uuid", equalTo(uuid)))
                .andExpect(jsonPath("$.email", equalTo(validRequest.getEmail())))
                .andExpect(jsonPath("$.type.uuid").exists())
                .andExpect(jsonPath("$.type.name", equalTo("OWNER")));
    }

    @Test
    void testUpdateUser_Success() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

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
        MvcResult createResult = mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        mockMvc.perform(delete("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/users/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateUser_InvalidData() throws Exception {
        CreateUserRequest invalidRequest = new CreateUserRequest();
        invalidRequest.setName(""); // Invalid: empty name
        // Ensure type is present so controller validation triggers for name instead of causing NPE
        invalidRequest.setType((com.restaurantmanager.api.model.UserTypeRef) validRequest.getType());

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

    private void seedDefaultUserTypes() {
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 1L, "OWNER");
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 2L, "CUSTOMER");
        jdbcTemplate.execute("alter table user_types alter column id restart with 3");
    }
}

