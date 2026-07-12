package com.restaurantmanager.api.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(name = "Restaurant", description = "Restaurant resource")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RestaurantDTO {

    @Schema(description = "Restaurant id", example = "1")
    private Long id;

    @Schema(description = "Restaurant name")
    @NotBlank
    @Size(max = 200)
    private String name;

    @Schema(description = "Address")
    @NotBlank
    @Size(max = 500)
    private String address;

    @Schema(description = "Cuisine type")
    @NotBlank
    @Size(max = 100)
    private String cuisineType;

    @Schema(description = "Opening hours")
    @NotBlank
    @Size(max = 200)
    private String openingHours;

    @Schema(description = "Owner user id")
    @NotNull
    private Long ownerUserId;

}

