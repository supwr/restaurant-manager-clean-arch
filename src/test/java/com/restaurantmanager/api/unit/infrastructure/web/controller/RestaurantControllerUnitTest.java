package com.restaurantmanager.api.unit.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.web.controller.RestaurantController;
import com.restaurantmanager.api.infrastructure.web.exception.GlobalExceptionHandler;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantMapper;
import com.restaurantmanager.api.model.RestaurantRequest;
import com.restaurantmanager.api.model.RestaurantResponse;
import com.restaurantmanager.api.model.RelatedUser;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RestaurantControllerUnitTest {

    @Mock private CreateRestaurantUseCase createRestaurantUseCase;
    @Mock private GetRestaurantUseCase getRestaurantUseCase;
    @Mock private ListRestaurantsUseCase listRestaurantsUseCase;
    @Mock private UpdateRestaurantUseCase updateRestaurantUseCase;
    @Mock private DeleteRestaurantUseCase deleteRestaurantUseCase;
    @Mock private RestaurantGateway restaurantGateway;
    @Mock private UserGateway userGateway;
    @Mock private RestaurantMapper restaurantMapper;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new RestaurantController(
            createRestaurantUseCase, getRestaurantUseCase, listRestaurantsUseCase, updateRestaurantUseCase, deleteRestaurantUseCase, restaurantGateway, userGateway, restaurantMapper
        )).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateRestaurant_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID ownerUuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Resto", "Street", "Italian", "9AM", 1L);
        RestaurantResponse response = new RestaurantResponse();
        response.setUuid(uuid);
        response.setName("Resto");
        RelatedUser owner = new RelatedUser();
        owner.setId(ownerUuid);
        owner.setName("Owner");
        response.setOwnerUser(owner);

        // Mock the owner without using the private constructor
        com.restaurantmanager.api.domain.model.User mockOwner = mock(com.restaurantmanager.api.domain.model.User.class);
        when(mockOwner.getUuid()).thenReturn(ownerUuid);
        when(mockOwner.getName()).thenReturn("Owner");

        when(userGateway.findByUuid(ownerUuid)).thenReturn(Optional.of(mockOwner));
        when(userGateway.findById(1L)).thenReturn(Optional.of(mockOwner));
        when(restaurantMapper.map(anyLong(), any(RestaurantRequest.class))).thenReturn(new Restaurant(null, "Resto", "Street", "Italian", "9AM", 1L));
        when(createRestaurantUseCase.execute(any(Restaurant.class))).thenReturn(domain);
        when(restaurantMapper.map(eq(domain), any(RelatedUser.class))).thenReturn(response);

        RestaurantRequest request = new RestaurantRequest();
        request.setName("Resto"); request.setAddress("Street"); request.setCuisineType("Italian"); request.setOpeningHours("9AM"); request.setOwnerUserUuid(ownerUuid);

        mockMvc.perform(post("/api/v1/restaurants").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Resto"));
    }

    @Test
    void testGetRestaurantByUuid_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID ownerUuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Resto", "Street", "Italian", "9AM", 1L);
        RestaurantResponse response = new RestaurantResponse();
        response.setUuid(uuid);
        response.setName("Resto");
        RelatedUser owner = new RelatedUser();
        owner.setId(ownerUuid);
        owner.setName("Owner");
        response.setOwnerUser(owner);

        // Mock the owner without using the private constructor
        com.restaurantmanager.api.domain.model.User mockOwner = mock(com.restaurantmanager.api.domain.model.User.class);
        when(mockOwner.getUuid()).thenReturn(ownerUuid);
        when(mockOwner.getName()).thenReturn("Owner");

        when(userGateway.findById(1L)).thenReturn(Optional.of(mockOwner));
        when(getRestaurantUseCase.execute(uuid)).thenReturn(domain);
        when(restaurantMapper.map(eq(domain), any(RelatedUser.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/restaurants/{uuid}", uuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("Resto"));
    }

    @Test
    void testListRestaurants_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        UUID ownerUuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Resto", "Street", "Italian", "9AM", 1L);
        RestaurantResponse response = new RestaurantResponse();
        response.setUuid(uuid);
        response.setName("Resto");
        RelatedUser owner = new RelatedUser();
        owner.setId(ownerUuid);
        owner.setName("Owner");
        response.setOwnerUser(owner);

        // Mock the owner without using the private constructor
        com.restaurantmanager.api.domain.model.User mockOwner = mock(com.restaurantmanager.api.domain.model.User.class);
        when(mockOwner.getUuid()).thenReturn(ownerUuid);
        when(mockOwner.getName()).thenReturn("Owner");

        when(listRestaurantsUseCase.execute(any(Pagination.class))).thenReturn(new PageResult<>(List.of(domain), 0, 20, 1L, 1));
        when(userGateway.findById(1L)).thenReturn(Optional.of(mockOwner));
        when(restaurantMapper.map(eq(domain), any(RelatedUser.class))).thenReturn(response);

        mockMvc.perform(get("/api/v1/restaurants"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteRestaurant_Success() throws Exception {
        UUID uuid = UUID.randomUUID();
        Restaurant domain = new Restaurant(1L, uuid, "Resto", "Street", "Italian", "9AM", 1L);
        doNothing().when(deleteRestaurantUseCase).execute(uuid);

        mockMvc.perform(delete("/api/v1/restaurants/{uuid}", uuid))
            .andExpect(status().isNoContent());
    }
}

