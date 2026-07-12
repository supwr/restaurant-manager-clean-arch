package com.restaurantmanager.api.infrastructure.persistence.adapter;

import com.restaurantmanager.api.application.port.UserTypePersistencePort;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.UserTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adapter that implements UserTypePersistencePort using Spring Data JPA.
 * This component bridges the application layer (which knows about UserTypePersistencePort)
 * and the infrastructure layer (which uses Spring Data and JPA).
 */
@Component
@RequiredArgsConstructor
public class UserTypePersistenceAdapter implements UserTypePersistencePort {

    private final UserTypeRepository repository;
    private final UserTypePersistenceMapper mapper;

    @Override
    public UserType save(UserType userType) {
        UserTypeEntity entity = mapper.toEntity(userType);
        UserTypeEntity saved = repository.save(entity);
        return mapper.toDomain(saved);
    }

    @Override
    public Optional<UserType> findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(String name) {
        return repository.findByName(name)
            .map(mapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
        return repository.findAll().stream()
            .map(mapper::toDomain)
            .toList();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }
}

