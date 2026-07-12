package com.restaurantmanager.api.application.port;

import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemPersistencePort {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    void deleteById(Long id);

    boolean existsById(Long id);
}

