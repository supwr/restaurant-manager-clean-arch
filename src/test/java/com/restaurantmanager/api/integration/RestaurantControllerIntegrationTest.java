package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.RestaurantRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

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

    private RestaurantRequest validRequest;

    @BeforeEach
    void setUp() {
        validRequest = new RestaurantRequest();
        validRequest.setName("Test Restaurant");
        validRequest.setAddress("123 Main St");
        validRequest.setCuisineType("Italian");
        validRequest.setOpeningHours("9AM-10PM");
        validRequest.setOwnerUserId(1L);
    }

    @Test
    void testCreateRestaurant_Success() throws Exception {
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Test Restaurant")))
                .andExpect(jsonPath("$.address", equalTo("123 Main St")))
                .andExpect(jsonPath("$.cuisineType", equalTo("Italian")))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testListRestaurants_Success() throws Exception {
        // Create a restaurant first
        mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        // List restaurants
        mockMvc.perform(get("/api/v1/restaurants")
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists())
                .andExpect(jsonPath("$[0].id").exists());
    }

    @Test
    void testGetRestaurantByUuid_Success() throws Exception {
        // Create a restaurant first
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Get restaurant by uuid
        mockMvc.perform(get("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Test Restaurant")))
                .andExpect(jsonPath("$.uuid", equalTo(uuid)))
                .andExpect(jsonPath("$.address", equalTo("123 Main St")));
    }

    @Test
    void testUpdateRestaurant_Success() throws Exception {
        // Create a restaurant first
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Update restaurant
        RestaurantRequest updateRequest = new RestaurantRequest();
        updateRequest.setName("Updated Restaurant");
        updateRequest.setAddress("456 Oak Ave");
        updateRequest.setCuisineType("French");
        updateRequest.setOpeningHours("10AM-11PM");
        updateRequest.setOwnerUserId(2L);

        mockMvc.perform(put("/api/v1/restaurants/{uuid}", uuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated Restaurant")))
                .andExpect(jsonPath("$.address", equalTo("456 Oak Ave")))
                .andExpect(jsonPath("$.cuisineType", equalTo("French")));
    }

    @Test
    void testDeleteRestaurant_Success() throws Exception {
        // Create a restaurant first
        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String uuid = extractUuidFromJson(responseBody);

        // Delete restaurant
        mockMvc.perform(delete("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/restaurants/{uuid}", uuid))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRestaurant_InvalidData() throws Exception {
        RestaurantRequest invalidRequest = new RestaurantRequest();
        invalidRequest.setName(""); // Invalid: empty name

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
}

