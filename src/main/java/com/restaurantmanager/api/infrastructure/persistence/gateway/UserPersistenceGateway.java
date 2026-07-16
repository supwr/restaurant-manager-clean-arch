package com.restaurantmanager.api.infrastructure.persistence.gateway;

import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class UserPersistenceGateway implements UserGateway {

    private final UserRepository repository;
    private final UserPersistenceMapper mapper;

    public UserPersistenceGateway(final UserRepository repository, final UserPersistenceMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public User save(final User user) {
        return mapper.toDomain(repository.save(mapper.toEntity(user)));
    }

    @Override
    public Optional<User> findById(final Long id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByUuid(final UUID uuid) {
        return repository.findByUuid(uuid).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByEmailIgnoreCase(final String email) {
        return repository.findByEmailIgnoreCase(email).map(mapper::toDomain);
    }

    @Override
    public Optional<User> findByLoginIgnoreCase(final String login) {
        return repository.findByLoginIgnoreCase(login).map(mapper::toDomain);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    @Transactional
    public void deleteById(final Long id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(final Long id) {
        return repository.existsById(id);
    }
}

