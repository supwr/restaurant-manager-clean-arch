package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.application.port.MenuItemPersistencePort;
import com.restaurantmanager.api.application.port.RestaurantPersistencePort;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.MenuItem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MenuItemService {

    private final MenuItemPersistencePort menuItemPort;
    private final RestaurantPersistencePort restaurantPort;

    public MenuItem create(MenuItem menuItem) {
        // Ensure restaurant exists
        if (!restaurantPort.existsById(menuItem.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant", menuItem.getRestaurantId().toString());
        }

        menuItem.validate();
        return menuItemPort.save(menuItem);
    }

    @Transactional(readOnly = true)
    public MenuItem getById(Long id) {
        return menuItemPort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MenuItem", id.toString()));
    }

    @Transactional(readOnly = true)
    public List<MenuItem> listByRestaurant(Long restaurantId) {
        if (!restaurantPort.existsById(restaurantId)) {
            throw new EntityNotFoundException("Restaurant", restaurantId.toString());
        }
        return menuItemPort.findByRestaurantId(restaurantId);
    }

    public MenuItem update(Long id, MenuItem menuItem) {
        MenuItem existing = menuItemPort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("MenuItem", id.toString()));

        // ensure restaurant exists
        if (!restaurantPort.existsById(menuItem.getRestaurantId())) {
            throw new EntityNotFoundException("Restaurant", menuItem.getRestaurantId().toString());
        }

        menuItem.validate();

        MenuItem updated = MenuItem.builder()
            .id(id)
            .restaurantId(menuItem.getRestaurantId())
            .name(menuItem.getName())
            .description(menuItem.getDescription())
            .price(menuItem.getPrice())
            .localOnly(menuItem.getLocalOnly())
            .photoPath(menuItem.getPhotoPath())
            .build();

        return menuItemPort.save(updated);
    }

    public void delete(Long id) {
        if (!menuItemPort.existsById(id)) {
            throw new EntityNotFoundException("MenuItem", id.toString());
        }
        menuItemPort.deleteById(id);
    }
}

