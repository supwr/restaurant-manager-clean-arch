package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.application.port.RestaurantPersistencePort;
import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.exception.ValidationException;
import com.restaurantmanager.api.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RestaurantService {

    private final RestaurantPersistencePort persistencePort;

    public Restaurant create(Restaurant restaurant) {
        restaurant.validate();
        return persistencePort.save(restaurant);
    }

    @Transactional(readOnly = true)
    public Restaurant getById(Long id) {
        return persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()));
    }

    @Transactional(readOnly = true)
    public List<Restaurant> listAll() {
        return persistencePort.findAll();
    }

    public Restaurant update(Long id, Restaurant restaurant) {
        Restaurant existing = persistencePort.findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Restaurant", id.toString()));

        restaurant.validate();

        Restaurant updated = Restaurant.builder()
            .id(id)
            .name(restaurant.getName())
            .address(restaurant.getAddress())
            .cuisineType(restaurant.getCuisineType())
            .openingHours(restaurant.getOpeningHours())
            .ownerUserId(restaurant.getOwnerUserId())
            .build();

        return persistencePort.save(updated);
    }

    public void delete(Long id) {
        if (!persistencePort.existsById(id)) {
            throw new EntityNotFoundException("Restaurant", id.toString());
        }
        persistencePort.deleteById(id);
    }
}

