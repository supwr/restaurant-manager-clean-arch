package com.restaurantmanager.api.application.port;

import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantPersistencePort {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}

