package com.restaurantmanager.api.infrastructure.persistence.gateway;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.MenuItemRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class MenuItemPersistenceGateway implements MenuItemGateway {

    private final MenuItemRepository repository;
    private final MenuItemPersistenceMapper mapper;

    public MenuItemPersistenceGateway(final MenuItemRepository repository, final MenuItemPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public MenuItem save(final MenuItem menuItem) {
        return mapper.toDomain(repository.save(mapper.toEntity(menuItem)));
    }

    @Override
    public Optional<MenuItem> findById(final Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<MenuItem> findByRestaurantId(final Long restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }
}


