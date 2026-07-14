package com.restaurantmanager.api.infrastructure.persistence.gateway;

import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserTypePersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.UserTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class UserTypePersistenceGateway implements UserTypeGateway {

    private final UserTypeRepository repository;
    private final UserTypePersistenceMapper mapper;

    public UserTypePersistenceGateway(final UserTypeRepository repository, final UserTypePersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public UserType save(final UserType userType) {
        return mapper.toDomain(repository.save(mapper.toEntity(userType)));
    }

    @Override
    public Optional<UserType> findById(final Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<UserType> findByName(final String name) {
        return repository.findByName(name).map(mapper::toDomain);
    }

    @Override
    public List<UserType> findAll() {
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
    public boolean existsByName(final String name) {
        return repository.existsByName(name);
    }
}


