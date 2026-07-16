package com.restaurantmanager.api.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.MenuItemRequest;
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

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.UUID;

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

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private String restaurantUuid;

    @BeforeEach
    void setUp() throws Exception {
        seedDefaultUserTypes();

        final UUID ownerUserUuid = createOwnerUserUuid();

        RestaurantRequest restaurantRequest = new RestaurantRequest();
        restaurantRequest.setName("Test Restaurant " + UUID.randomUUID());
        restaurantRequest.setAddress("123 Main St");
        restaurantRequest.setCuisineType("Italian");
        restaurantRequest.setOpeningHours("9AM-10PM");
        com.restaurantmanager.api.model.OwnerRequest ownerReq = new com.restaurantmanager.api.model.OwnerRequest();
        ownerReq.setId(ownerUserUuid);
        restaurantRequest.setOwner(ownerReq);

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
                .andExpect(jsonPath("$.restaurant.id").exists())
                .andExpect(jsonPath("$.restaurant.name").exists())
                .andExpect(jsonPath("$.id").doesNotExist());
    }

    @Test
    void testListMenuItems_Success() throws Exception {
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

        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid)
                .param("page", "0")
                .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].name").exists())
                .andExpect(jsonPath("$[0].uuid").exists())
                .andExpect(jsonPath("$[0].restaurant.id").exists())
                .andExpect(jsonPath("$[0].id").doesNotExist());
    }

    @Test
    void testGetMenuItemByUuid_Success() throws Exception {
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

        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", equalTo("Burger")))
                .andExpect(jsonPath("$.uuid", equalTo(menuItemUuid)))
                .andExpect(jsonPath("$.restaurant.id").exists())
                .andExpect(jsonPath("$.restaurant.name").exists());
    }

    @Test
    void testUpdateMenuItem_Success() throws Exception {
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
                .andExpect(jsonPath("$.uuid", equalTo(menuItemUuid)))
                .andExpect(jsonPath("$.restaurant.id").exists())
                .andExpect(jsonPath("$.restaurant.name").exists());
    }

    @Test
    void testDeleteMenuItem_Success() throws Exception {
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

        mockMvc.perform(delete("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
                .andExpect(status().isNoContent());

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

    private UUID createOwnerUserUuid() throws Exception {
        final String suffix = UUID.randomUUID().toString().substring(0, 8);
        final CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("Owner " + suffix);
        createUserRequest.setEmail("owner-" + suffix + "@example.com");
        createUserRequest.setLogin("owner-" + suffix);
        // Prefer seeded user type if present, otherwise create via API and use its uuid
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
    }

    private void seedDefaultUserTypes() {
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 1L, "OWNER");
        jdbcTemplate.update("merge into user_types (id, uuid, name, created_at, updated_at) key(id) values (?, random_uuid(), ?, current_timestamp, current_timestamp)", 2L, "CUSTOMER");
        jdbcTemplate.execute("alter table user_types alter column id restart with 3");
    }
}

