package com.restaurantmanager.api.infrastructure.persistence.adapter;

import com.restaurantmanager.api.application.port.MenuItemPersistencePort;
import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.persistence.mapper.MenuItemPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.entity.MenuItemEntity;
import com.restaurantmanager.api.infrastructure.persistence.repository.MenuItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class MenuItemPersistenceAdapter implements MenuItemPersistencePort {

    private final MenuItemRepository repository;
    private final MenuItemPersistenceMapper mapper;

    @Override
    public MenuItem save(MenuItem menuItem) {
        MenuItemEntity entity = mapper.toEntity(menuItem);
        MenuItemEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<MenuItem> findById(Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<MenuItem> findByRestaurantId(Long restaurantId) {
        return repository.findByRestaurantId(restaurantId).stream().map(mapper::toDomain).toList();
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

