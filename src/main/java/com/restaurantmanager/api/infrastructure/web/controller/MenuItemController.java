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
import com.restaurantmanager.api.model.MenuItemRequest;
import com.restaurantmanager.api.model.MenuItemResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@RestController
public class MenuItemController implements MenuItemsApi {

    private final CreateMenuItemUseCase createMenuItemUseCase;
    private final GetMenuItemUseCase getMenuItemUseCase;
    private final ListMenuItemsUseCase listMenuItemsUseCase;
    private final UpdateMenuItemUseCase updateMenuItemUseCase;
    private final DeleteMenuItemUseCase deleteMenuItemUseCase;

    public MenuItemController(
        final CreateMenuItemUseCase createMenuItemUseCase,
        final GetMenuItemUseCase getMenuItemUseCase,
        final ListMenuItemsUseCase listMenuItemsUseCase,
        final UpdateMenuItemUseCase updateMenuItemUseCase,
        final DeleteMenuItemUseCase deleteMenuItemUseCase
    ) {
        this.createMenuItemUseCase = Objects.requireNonNull(createMenuItemUseCase);
        this.getMenuItemUseCase = Objects.requireNonNull(getMenuItemUseCase);
        this.listMenuItemsUseCase = Objects.requireNonNull(listMenuItemsUseCase);
        this.updateMenuItemUseCase = Objects.requireNonNull(updateMenuItemUseCase);
        this.deleteMenuItemUseCase = Objects.requireNonNull(deleteMenuItemUseCase);
    }

    @Override
    public ResponseEntity<MenuItemResponse> createMenuItem(final Long restaurantId, @Valid final MenuItemRequest menuItemRequest) {
        final MenuItem created = createMenuItemUseCase.execute(toDomain(restaurantId, null, menuItemRequest));
        return ResponseEntity.status(HttpStatus.CREATED).body(toResponse(created));
    }

    @Override
    public ResponseEntity<Void> deleteMenuItem(final Long restaurantId, final Long id) {
        deleteMenuItemUseCase.execute(restaurantId, id);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<MenuItemResponse> getMenuItemById(final Long restaurantId, final Long id) {
        return ResponseEntity.ok(toResponse(getMenuItemUseCase.execute(restaurantId, id)));
    }

    @Override
    public ResponseEntity<List<MenuItemResponse>> listMenuItems(final Long restaurantId, final Integer page, final Integer size) {
        final PageResult<MenuItem> result = listMenuItemsUseCase.execute(restaurantId, new Pagination(page == null ? 0 : page, size == null ? 20 : size, "id"));
        return ResponseEntity.ok(result.getContent().stream().map(this::toResponse).toList());
    }

    @Override
    public ResponseEntity<MenuItemResponse> updateMenuItem(final Long restaurantId, final Long id, @Valid final MenuItemRequest menuItemRequest) {
        return ResponseEntity.ok(toResponse(updateMenuItemUseCase.execute(restaurantId, id, toDomain(restaurantId, id, menuItemRequest))));
    }

    private MenuItem toDomain(final Long restaurantId, final Long id, final MenuItemRequest request) {
        return new MenuItem(
            id,
            restaurantId,
            request.getName(),
            request.getDescription(),
            request.getPrice(),
            request.getLocalOnly(),
            request.getPhotoPath()
        );
    }

    private MenuItemResponse toResponse(final MenuItem menuItem) {
        final MenuItemResponse response = new MenuItemResponse();
        response.setId(menuItem.getId());
        response.setRestaurantId(menuItem.getRestaurantId());
        response.setName(menuItem.getName());
        response.setDescription(menuItem.getDescription());
        response.setPrice(menuItem.getPrice());
        response.setLocalOnly(menuItem.getLocalOnly());
        response.setPhotoPath(menuItem.getPhotoPath());
        return response;
    }
}

