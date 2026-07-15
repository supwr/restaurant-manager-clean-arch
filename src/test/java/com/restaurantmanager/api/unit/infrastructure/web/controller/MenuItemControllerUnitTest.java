package com.restaurantmanager.api.unit.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.usecase.menuitem.create.CreateMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.delete.DeleteMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.get.GetMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.list.ListMenuItemsUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.update.UpdateMenuItemUseCase;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.web.controller.MenuItemController;
import com.restaurantmanager.api.infrastructure.web.exception.GlobalExceptionHandler;
import com.restaurantmanager.api.infrastructure.web.mapper.MenuItemMapper;
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MenuItemControllerUnitTest {

    @Mock private CreateMenuItemUseCase createMenuItemUseCase;
    @Mock private GetMenuItemUseCase getMenuItemUseCase;
    @Mock private ListMenuItemsUseCase listMenuItemsUseCase;
    @Mock private UpdateMenuItemUseCase updateMenuItemUseCase;
    @Mock private DeleteMenuItemUseCase deleteMenuItemUseCase;
    @Mock private MenuItemMapper menuItemMapper;
    @Mock private MenuItemGateway menuItemGateway;
    @Mock private RestaurantGateway restaurantGateway;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuItemController(
            createMenuItemUseCase, getMenuItemUseCase, listMenuItemsUseCase, updateMenuItemUseCase, deleteMenuItemUseCase, menuItemMapper, menuItemGateway, restaurantGateway
        )).setControllerAdvice(new GlobalExceptionHandler()).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testCreateMenuItem_Success() throws Exception {
        UUID restaurantUuid = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(10L, restaurantUuid, "Resto", "Street", "Italian", "9AM", 1L);
        MenuItem domain = new MenuItem(1L, UUID.randomUUID(), 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        MenuItemResponse response = new MenuItemResponse(); response.setId(1L); response.setName("Pizza");
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemMapper.map(10L, any(MenuItemRequest.class))).thenReturn(new MenuItem(null, 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza"));
        when(createMenuItemUseCase.execute(any(MenuItem.class))).thenReturn(domain);
        when(menuItemMapper.map(domain)).thenReturn(response);

        MenuItemRequest request = new MenuItemRequest(); request.setName("Pizza"); request.setDescription("Tasty"); request.setPrice(new BigDecimal("10.50")); request.setLocalOnly(true); request.setPhotoPath("/pizza");
        mockMvc.perform(post("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid).contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.name").value("Pizza"));
    }

    @Test
    void testListMenuItems_Success() throws Exception {
        UUID restaurantUuid = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(10L, restaurantUuid, "Resto", "Street", "Italian", "9AM", 1L);
        MenuItem domain = new MenuItem(1L, UUID.randomUUID(), 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        MenuItemResponse response = new MenuItemResponse(); response.setId(1L); response.setName("Pizza");
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(listMenuItemsUseCase.execute(eq(10L), any())).thenReturn(new PageResult<>(List.of(domain), 0, 20, 1L, 1));
        when(menuItemMapper.map(domain)).thenReturn(response);

        mockMvc.perform(get("/api/v1/restaurants/{restaurantUuid}/menu-items", restaurantUuid))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteMenuItem_Success() throws Exception {
        UUID restaurantUuid = UUID.randomUUID();
        UUID menuItemUuid = UUID.randomUUID();
        Restaurant restaurant = new Restaurant(10L, restaurantUuid, "Resto", "Street", "Italian", "9AM", 1L);
        MenuItem domain = new MenuItem(1L, menuItemUuid, 10L, "Pizza", "Tasty", new BigDecimal("10.50"), true, "/pizza");
        when(restaurantGateway.findByUuid(restaurantUuid)).thenReturn(Optional.of(restaurant));
        when(menuItemGateway.findByUuid(menuItemUuid)).thenReturn(Optional.of(domain));

        mockMvc.perform(delete("/api/v1/restaurants/{restaurantUuid}/menu-items/{menuItemUuid}", restaurantUuid, menuItemUuid))
            .andExpect(status().isNoContent());
    }
}

