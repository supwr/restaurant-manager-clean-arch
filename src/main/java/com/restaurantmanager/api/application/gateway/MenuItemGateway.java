package com.restaurantmanager.api.application.gateway;

import com.restaurantmanager.api.domain.model.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MenuItemGateway {

    MenuItem save(MenuItem menuItem);

    Optional<MenuItem> findById(Long id);

    Optional<MenuItem> findByUuid(UUID uuid);

    List<MenuItem> findByRestaurantId(Long restaurantId);

    void deleteById(Long id);

    void deleteByUuid(UUID uuid);

    boolean existsById(Long id);

    boolean existsByUuid(UUID uuid);
}

