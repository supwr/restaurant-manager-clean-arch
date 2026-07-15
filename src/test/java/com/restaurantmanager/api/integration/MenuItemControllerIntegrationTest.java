package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.MenuItemRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("it")
class MenuItemControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private String restaurantUuid;

    @BeforeEach
    void setUp() throws Exception {
        // Create a restaurant first
        RestaurantRequest restaurantRequest = new RestaurantRequest();
        restaurantRequest.setName("Test Restaurant");
        restaurantRequest.setAddress("123 Main St");
        restaurantRequest.setCuisineType("Italian");
        restaurantRequest.setOpeningHours("9AM-10PM");
        restaurantRequest.setOwnerUserId(1L);

        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(restaurantRequest)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        restaurantUuid = extractUuidFromJson(responseBody);
    }

    @Test
    void testCreateMenuItem_Success() throws Exception {
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pasta");
        request.setDescription("Delicious pasta");
        request.setPrice(new BigDecimal("15.99"));
        request.setLocalOnly(true);
        request.setPhotoPath("/pasta");

        mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", equalTo("Pasta")))
                .andExpect(jsonPath("$.price", equalTo(15.99)))
                .andExpect(jsonPath("$.uuid").exists())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void testListMenuItems_Success() throws Exception {
        // Create a menu item first
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Pizza");
        request.setDescription("Delicious pizza");
        request.setPrice(new BigDecimal("12.99"));
        request.setLocalOnly(false);
        request.setPhotoPath("/pizza");

        mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // List menu items
        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists());
    }

    @Test
    void testGetMenuItemByUuid_Success() throws Exception {
        // Create a menu item first
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Burger");
        request.setDescription("Tasty burger");
        request.setPrice(new BigDecimal("9.99"));
        request.setLocalOnly(true);
        request.setPhotoPath("/burger");

        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String menuItemUuid = extractUuidFromJson(responseBody);

        // Get menu item by uuid
        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Burger")))
                .andExpect(jsonPath("$.uuid", equalTo(menuItemUuid)));
    }

    @Test
    void testUpdateMenuItem_Success() throws Exception {
        // Create a menu item first
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Salad");
        request.setDescription("Fresh salad");
        request.setPrice(new BigDecimal("8.99"));
        request.setLocalOnly(false);
        request.setPhotoPath("/salad");

        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String menuItemUuid = extractUuidFromJson(responseBody);

        // Update menu item
        MenuItemRequest updateRequest = new MenuItemRequest();
        updateRequest.setName("Updated Salad");
        updateRequest.setDescription("Updated fresh salad");
        updateRequest.setPrice(new BigDecimal("9.99"));
        updateRequest.setLocalOnly(true);
        updateRequest.setPhotoPath("/updated-salad");

        mockMvc.perform(put("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Updated Salad")))
                .andExpect(jsonPath("$.price", equalTo(9.99)));
    }

    @Test
    void testDeleteMenuItem_Success() throws Exception {
        // Create a menu item first
        MenuItemRequest request = new MenuItemRequest();
        request.setName("Dessert");
        request.setDescription("Sweet dessert");
        request.setPrice(new BigDecimal("5.99"));
        request.setLocalOnly(false);
        request.setPhotoPath("/dessert");

        MvcResult createResult = mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = createResult.getResponse().getContentAsString();
        String menuItemUuid = extractUuidFromJson(responseBody);

        // Delete menu item
        mockMvc.perform(delete("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
                .andExpect(status().isNoContent());

        // Verify it's deleted
        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
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

