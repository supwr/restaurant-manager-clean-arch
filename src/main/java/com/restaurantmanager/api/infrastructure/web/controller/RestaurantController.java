package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.application.usecase.RestaurantService;
import com.restaurantmanager.api.infrastructure.web.dto.RestaurantDTO;
import com.restaurantmanager.api.infrastructure.web.mapper.RestaurantWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/restaurants")
@RequiredArgsConstructor
@Tag(name = "Restaurants", description = "Restaurant management API")
public class RestaurantController {

    private static final Logger logger = LoggerFactory.getLogger(RestaurantController.class);

    private final RestaurantService restaurantService;
    private final RestaurantWebMapper webMapper;

    @PostMapping
    @Operation(summary = "Create a restaurant")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Restaurant created"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    public ResponseEntity<RestaurantDTO> create(@Valid @RequestBody RestaurantDTO request) {
        logger.info("Creating restaurant: {}", request.getName());
        var domain = webMapper.toDomain(request);
        var created = restaurantService.create(domain);
        var response = webMapper.toDTO(created);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestaurantDTO> getById(@PathVariable Long id) {
        logger.info("Get restaurant by id: {}", id);
        var r = restaurantService.getById(id);
        return ResponseEntity.ok(webMapper.toDTO(r));
    }

    @GetMapping
    public ResponseEntity<List<RestaurantDTO>> listAll() {
        var list = restaurantService.listAll().stream().map(webMapper::toDTO).toList();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RestaurantDTO> update(@PathVariable Long id, @Valid @RequestBody RestaurantDTO request) {
        logger.info("Update restaurant id: {}", id);
        var domain = webMapper.toDomain(request);
        var updated = restaurantService.update(id, domain);
        return ResponseEntity.ok(webMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Delete restaurant id: {}", id);
        restaurantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

