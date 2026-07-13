package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.application.usecase.menuitem.MenuItemService;
import com.restaurantmanager.api.infrastructure.web.dto.MenuItemDTO;
import com.restaurantmanager.api.infrastructure.web.mapper.MenuItemWebMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MenuItemPublicController {

    private static final Logger logger = LoggerFactory.getLogger(MenuItemPublicController.class);

    private final MenuItemService menuItemService;
    private final MenuItemWebMapper webMapper;

    @GetMapping("/{id}")
    public ResponseEntity<MenuItemDTO> getById(@PathVariable Long id) {
        var item = menuItemService.getById(id);
        return ResponseEntity.ok(webMapper.toDTO(item));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuItemDTO> update(@PathVariable Long id, @Valid @RequestBody MenuItemDTO request) {
        // Note: request.restaurantId must be present and valid
        var domain = webMapper.toDomain(request);
        var updated = menuItemService.update(id, domain);
        return ResponseEntity.ok(webMapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        menuItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

