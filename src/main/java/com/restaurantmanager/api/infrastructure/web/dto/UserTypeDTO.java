package com.restaurantmanager.api.infrastructure.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * DTO for UserType request/response in the API.
 * Used for JSON serialization/deserialization.
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(name = "UserType", description = "User type classification (Owner, Customer, etc.)")
public class UserTypeDTO {

    @Schema(description = "User type ID", example = "1")
    @JsonProperty("id")
    private Long id;

    @Schema(description = "User type name", example = "Restaurant Owner")
    @JsonProperty("name")
    @NotBlank(message = "User type name is required")
    @Size(min = 1, max = 100, message = "User type name must be between 1 and 100 characters")
    private String name;

    @Schema(description = "User type observation or note", example = "Owner of a restaurant establishment")
    @JsonProperty("observation")
    @Size(max = 500, message = "Observation must not exceed 500 characters")
    private String observation;

    @Schema(description = "Creation timestamp", example = "2026-07-12T10:30:00")
    @JsonProperty("createdAt")
    private java.time.LocalDateTime createdAt;

    @Schema(description = "Last update timestamp", example = "2026-07-12T10:30:00")
    @JsonProperty("updatedAt")
    private java.time.LocalDateTime updatedAt;
}

