package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.MenuItemsApi;
import com.restaurantmanager.api.application.usecase.menuitem.create.CreateMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.delete.DeleteMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.get.GetMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.list.ListMenuItemsUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.update.UpdateMenuItemUseCase;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.domain.model.PageResult;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import com.restaurantmanager.api.model.RelatedRestaurant;
import com.restaurantmanager.api.infrastructure.web.mapper.MenuItemMapper;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class MenuItemController implements MenuItemsApi {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetMenuItemUseCase getMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;
    private final MenuItemMapper menuItemMapper;
    private final MenuItemGateway menuItemGateway;
    private final RestaurantGateway restaurantGateway;

    public MenuItemController(
        final CreateMenuItemUseCase createMenuItemUseCase,
        final GetMenuItemUseCase getMenuItemUseCase,
        final ListMenuItemsUseCase listMenuItemsUseCase,
        final UpdateMenuItemUseCase updateMenuItemUseCase,
        final DeleteMenuItemUseCase deleteMenuItemUseCase,
        final MenuItemMapper menuItemMapper,
        final MenuItemGateway menuItemGateway,
        final RestaurantGateway restaurantGateway
    ) {
        this.createMenuItemUseCase = Objects.requireNonNull(createMenuItemUseCase);
        this.getMenuItemUseCase = Objects.requireNonNull(getMenuItemUseCase);
        this.listMenuItemsUseCase = Objects.requireNonNull(listMenuItemsUseCase);
        this.updateMenuItemUseCase = Objects.requireNonNull(updateMenuItemUseCase);
        this.deleteMenuItemUseCase = Objects.requireNonNull(deleteMenuItemUseCase);
        this.menuItemMapper = Objects.requireNonNull(menuItemMapper);
        this.menuItemGateway = Objects.requireNonNull(menuItemGateway);
        this.restaurantGateway = Objects.requireNonNull(restaurantGateway);
    }

    @Override
    public ResponseEntity<MenuItemResponse> createMenuItem(final UUID restaurantUuid, @Valid final MenuItemRequest menuItemRequest) {
        final MenuItem created = createMenuItemUseCase.execute(restaurantUuid, menuItemMapper.map(menuItemRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created, restaurantUuid));
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(final UUID restaurantUuid, final UUID menuItemUuid) {
        deleteMenuItemUseCase.execute(restaurantUuid, menuItemUuid);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItemById(final UUID restaurantUuid, final UUID menuItemUuid) {
        return ResponseEntity.ok(toResponse(getMenuItemUseCase.execute(restaurantUuid, menuItemUuid), restaurantUuid));
    }

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(final UUID restaurantUuid, final Integer page, final Integer size) {
        final PageResult<MenuItem> result = listMenuItemsUseCase.execute(restaurantUuid, new Pagination(page == null ? 0 : page, size == null ? 20 : size, "id"));
        return ResponseEntity.ok(result.getContent().stream().map(menuItem -> toResponse(menuItem, restaurantUuid)).toList());
    }

    @Override
    public ResponseEntity<MenuItemResponse> updateMenuItem(final UUID restaurantUuid, final UUID menuItemUuid, @Valid final MenuItemRequest menuItemRequest) {
        return ResponseEntity.ok(toResponse(updateMenuItemUseCase.execute(restaurantUuid, menuItemUuid, menuItemMapper.map(menuItemRequest)), restaurantUuid));
    }

    private MenuItemResponse toResponse(final MenuItem menuItem, final UUID restaurantUuid) {
        final var restaurant = restaurantGateway.findByUuid(restaurantUuid)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", restaurantUuid.toString()));

        final RelatedRestaurant relatedRestaurant = new RelatedRestaurant();
        relatedRestaurant.setId(restaurant.getUuid());
        relatedRestaurant.setName(restaurant.getName());
        return menuItemMapper.map(menuItem, relatedRestaurant);
    }
}
