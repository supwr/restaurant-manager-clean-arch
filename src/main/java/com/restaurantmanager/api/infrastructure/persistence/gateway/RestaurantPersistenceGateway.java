package com.restaurantmanager.api.infrastructure.persistence.gateway;

import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.infrastructure.persistence.mapper.RestaurantPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.RestaurantRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class RestaurantPersistenceGateway implements RestaurantGateway {

    private final RestaurantRepository repository;
    private final RestaurantPersistenceMapper mapper;

    public RestaurantPersistenceGateway(final RestaurantRepository repository, final RestaurantPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Restaurant save(final Restaurant restaurant) {
        return mapper.toDomain(repository.save(mapper.toEntity(restaurant)));
    }

    @Override
    public Optional<Restaurant> findById(final Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Restaurant> findByUuid(final java.util.UUID uuid) {
        return repository.findByUuid(uuid).map(mapper::toDomain);
    }

    @Override
    public List<Restaurant> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByUuid(final java.util.UUID uuid) {
        return repository.existsByUuid(uuid);
    }
}


