package com.restaurantmanager.api.infrastructure.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Schema(name = "MenuItem", description = "Menu item resource")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuItemDTO {

    @Schema(description = "Menu item id", example = "1")
    private Long id;

    @Schema(description = "Restaurant id this item belongs to")
    @NotNull
    private Long restaurantId;

    @Schema(description = "Name of the menu item")
    @NotBlank
    @Size(max = 200)
    private String name;

    @Schema(description = "Description of the menu item")
    @NotBlank
    @Size(max = 1000)
    private String description;

    @Schema(description = "Price")
    @NotNull
    private BigDecimal price;

    @Schema(description = "Available only for on-site consumption")
    @NotNull
    private Boolean localOnly;

    @Schema(description = "Photo path")
    @NotNull
    @Size(max = 1000)
    private String photoPath;

}

