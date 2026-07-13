package com.restaurantmanager.api.infrastructure.persistence.adapter;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.persistence.entity.RestaurantEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements RestaurantGateway {

    private final RestaurantRepository repository;
    private final RestaurantPersistenceMapper mapper;

    @Override
    public Restaurant save(Restaurant restaurant) {
        RestaurantEntity entity = mapper.toEntity(restaurant);
        RestaurantEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<Restaurant> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }
}

