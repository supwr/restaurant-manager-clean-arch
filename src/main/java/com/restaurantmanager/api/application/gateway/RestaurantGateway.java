package com.restaurantmanager.api.application.gateway;

import com.restaurantmanager.api.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface RestaurantGateway {

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Long id);

    List<Restaurant> findAll();

    void deleteById(Long id);

    boolean existsById(Long id);
}

