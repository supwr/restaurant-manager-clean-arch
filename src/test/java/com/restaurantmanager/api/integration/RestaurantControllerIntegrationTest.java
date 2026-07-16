package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.UserType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class RestaurantControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RestaurantRequest validRequest;

    @BeforeEach
    void setUp() {
        seedDefaultUserTypes();

        final String suffix = UUID.randomUUID().toString().substring(0, 8);
        validRequest = new RestaurantRequest();
        validRequest.setName("Test Restaurant " + suffix);
        validRequest.setAddress("123 Main St");
        validRequest.setCuisineType("Italian");
        validRequest.setOpeningHours("9AM-10PM");
        com.restaurantmanager.api.model.OwnerRequest ownerReq = new com.restaurantmanager.api.model.OwnerRequest();
        ownerReq.setId(createOwnerUserUuid());
        validRequest.setOwner(ownerReq);
    }

    @Test
    void testCreateRestaurant_Success() throws Exception {
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo(validRequest.getName())))
                .andExpect(jsonPath("$.address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.cuisineType", equalTo("Italian")))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.owner.id").exists())
                .andExpect(jsonPath("$.owner.name").exists())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testListRestaurants_Success() throws Exception {
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        mockMvc.perform(get("/api/v1/restaurants")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists())
                .andExpect(jsonPath("$[0].owner.id").exists())
                .andExpect(jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void testGetRestaurantByUuid_Success() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        mockMvc.perform(get("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo(validRequest.getName())))
                .andExpect(jsonPath("$.uuid", equalTo(uuid)))
                .andExpect(jsonPath("$.address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.owner.id").exists())
                .andExpect(jsonPath("$.owner.name").exists());
    }

    @Test
    void testUpdateRestaurant_Success() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        RestaurantRequest updateRequest = new RestaurantRequest();
        updateRequest.setName("Updated Restaurant");
        updateRequest.setAddress("456 Oak Ave");
        updateRequest.setCuisineType("French");
        updateRequest.setOpeningHours("10AM-11PM");
        com.restaurantmanager.api.model.OwnerRequest updateOwnerReq = new com.restaurantmanager.api.model.OwnerRequest();
        updateOwnerReq.setId(((com.restaurantmanager.api.model.OwnerRequest) validRequest.getOwner()).getId());
        updateRequest.setOwner(updateOwnerReq);

        mockMvc.perform(put("/api/v1/restaurants/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated Restaurant")))
                .andExpect(jsonPath("$.address", equalTo("456 Oak Ave")))
                .andExpect(jsonPath("$.cuisineType", equalTo("French")))
                .andExpect(jsonPath("$.owner.id").exists());
    }

    @Test
    void testDeleteRestaurant_Success() throws Exception {
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        mockMvc.perform(delete("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRestaurant_InvalidData() throws Exception {
        RestaurantRequest invalidRequest = new RestaurantRequest();
        invalidRequest.setName(""); // Invalid: empty name
        invalidRequest.setAddress("123 Main St");
        invalidRequest.setCuisineType("Italian");
        invalidRequest.setOpeningHours("9AM-10PM");
        invalidRequest.setOwner(((com.restaurantmanager.api.model.OwnerRequest) validRequest.getOwner()));

        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetNonExistentRestaurant() throws Exception {
        String fakeUuid = "00000000-0000-0000-0000-000000000000";

        mockMvc.perform(get("/api/v1/restaurants/{uuid}", fakeUuid))
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

    private UUID createOwnerUserUuid() {
        try {
            final String suffix = UUID.randomUUID().toString().substring(0, 8);
            final CreateUserRequest createUserRequest = new CreateUserRequest();
            createUserRequest.setName("Owner " + suffix);
            createUserRequest.setEmail("owner-" + suffix + "@example.com");
            createUserRequest.setLogin("owner-" + suffix);
            // Try to reuse seeded USER_TYPE if exists, otherwise create it via API and use its uuid
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
            final com.restaurantmanager.api.model.UserTypeRef userType = new com.restaurantmanager.api.model.UserTypeRef();
            userType.setId(UUID.fromString(typeUuid));
            createUserRequest.setType(userType);

            final MvcResult result = mockMvc.perform(post("/api/v1/users")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(createUserRequest)))
                    .andExpect(status().isCreated())
                    .andReturn();

            final String createdUuid = extractUuidFromJson(result.getResponse().getContentAsString());
            if (createdUuid == null) {
                throw new IllegalStateException("Owner user creation did not return a UUID");
            }
            return UUID.fromString(createdUuid);
        } catch (Exception ex) {
            throw new IllegalStateException("Failed to create owner user for integration test", ex);
        }
    }

    private void seedDefaultUserTypes() {
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 1L, "OWNER");
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 2L, "CUSTOMER");
        jdbcTemplate.execute("alter table user_types alter column id restart with 3");
    }
}

