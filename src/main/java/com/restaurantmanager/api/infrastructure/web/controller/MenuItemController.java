package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.application.usecase.MenuItemService;
import com.restaurantmanager.api.infrastructure.web.dto.MenuItemDTO;
import com.restaurantmanager.api.infrastructure.web.mapper.MenuItemWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants/{restaurantId}/menu-items")
@RequiredArgsConstructor
@Tag(name = "Menu Items", description = "Menu item management API")
public class MenuItemController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemController.class);

    private final MenuItemService menuItemService;
    private final MenuItemWebMapper webMapper;

    @PostMapping
    @Operation(summary = "Create a menu item for a restaurant")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Menu item created"),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "404", description = "Restaurant not found")
    })
    public ResponseEntity<MenuItemDTO> create(
        @PathVariable Long restaurantId,
        @Valid @RequestBody MenuItemDTO request
    ) {
        logger.info("Create menu item for restaurant {}: {}", restaurantId, request.getName());
        // ensure path id and body id consistency
        var dtoWithRestaurant = MenuItemDTO.builder()
            .restaurantId(restaurantId)
            .name(request.getName())
            .description(request.getDescription())
            .price(request.getPrice())
            .localOnly(request.getLocalOnly())
            .photoPath(request.getPhotoPath())
            .build();

        var domain = webMapper.toDomain(dtoWithRestaurant);
        var created = menuItemService.create(domain);
        var response = webMapper.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<MenuItemDTO>> listByRestaurant(@PathVariable Long restaurantId) {
        var items = menuItemService.listByRestaurant(restaurantId).stream().map(webMapper::toDTO).toList();
        return ResponseEntity.ok(items);
    }

    // Single-item endpoints are exposed under /api/v1/menu-items (see MenuItemPublicController)
}

