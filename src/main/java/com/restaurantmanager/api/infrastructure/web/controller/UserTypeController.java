package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.application.usecase.usertype.UserTypeService;
import com.restaurantmanager.api.infrastructure.web.dto.UserTypeDTO;
import com.restaurantmanager.api.infrastructure.web.mapper.UserTypeWebMapper;
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
public class UserTypeController {

    private static final Logger logger = LoggerFactory.getLogger(UserTypeController.class);

    private final UserTypeService userTypeService;
    private final UserTypeWebMapper webMapper;


    @PostMapping
    @Operation(summary = "Create a new user type")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "User type created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<UserTypeDTO> create(@Valid @RequestBody UserTypeDTO request) {
        logger.info("Creating user type with name: {}", request.getName());

        var domainModel = webMapper.toDomain(request);
        var created = userTypeService.create(domainModel);
        var response = webMapper.toDTO(created);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Retrieves a user type by ID.
     * @param id the user type ID
     * @return the user type with HTTP 200, or error response if not found
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get a user type by ID")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User type found"),
        @ApiResponse(responseCode = "404", description = "User type not found")
    })
    public ResponseEntity<UserTypeDTO> getById(@PathVariable Long id) {
        logger.info("Retrieving user type with id: {}", id);

        var userType = userTypeService.getById(id);
        var response = webMapper.toDTO(userType);

        return ResponseEntity.ok(response);
    }

    /**
     * Lists all user types.
     * @return a list of all user types
     */
    @GetMapping
    @Operation(summary = "List all user types")
    @ApiResponse(responseCode = "200", description = "List of user types")
    public ResponseEntity<List<UserTypeDTO>> listAll() {
        logger.info("Listing all user types");

        var userTypes = userTypeService.listAll();
        var response = userTypes.stream()
            .map(webMapper::toDTO)
            .toList();

        return ResponseEntity.ok(response);
    }

    /**
     * Updates a user type.
     * @param id the user type ID
     * @param request the updated user type data
     * @return the updated user type
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update a user type")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "User type updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid request data"),
        @ApiResponse(responseCode = "404", description = "User type not found")
    })
    public ResponseEntity<UserTypeDTO> update(
        @PathVariable Long id,
        @Valid @RequestBody UserTypeDTO request
    ) {
        logger.info("Updating user type with id: {}", id);

        var domainModel = webMapper.toDomain(request);
        var updated = userTypeService.update(id, domainModel);
        var response = webMapper.toDTO(updated);

        return ResponseEntity.ok(response);
    }

    /**
     * Deletes a user type.
     * @param id the user type ID
     * @return HTTP 204 No Content on success
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user type")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "User type deleted successfully"),
        @ApiResponse(responseCode = "404", description = "User type not found")
    })
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        logger.info("Deleting user type with id: {}", id);

        userTypeService.delete(id);

        return ResponseEntity.noContent().build();
    }
}

