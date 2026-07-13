package com.restaurantmanager.api.application.usecase.user.create;

import br.com.restaurantmanager.api.adapters.outbound.persistence.entity.UserJpaEntity;
import br.com.restaurantmanager.api.adapters.outbound.persistence.mapper.UserPersistenceMapper;
import br.com.restaurantmanager.api.domain.exception.DomainException;
import br.com.restaurantmanager.api.domain.factory.UserFactory;
import br.com.restaurantmanager.api.domain.model.Address;
import br.com.restaurantmanager.api.domain.model.User;
import br.com.restaurantmanager.api.ports.outbound.encryption.EncryptionService;
import br.com.restaurantmanager.api.ports.outbound.persistence.UserRepository;

import java.time.Instant;
import java.util.Objects;

public class CreateUserUseCase {

	private final UserRepository userRepository;
	private final UserPersistenceMapper userPersistenceMapper;
	private final EncryptionService encryptionService;

	public CreateUserUseCase(
			final UserRepository userRepository,
			final UserPersistenceMapper userPersistenceMapper,
			final EncryptionService encryptionService
	) {
		this.userRepository = Objects.requireNonNull(userRepository);
		this.userPersistenceMapper = Objects.requireNonNull(userPersistenceMapper);
		this.encryptionService = Objects.requireNonNull(encryptionService);
	}

	public CreateUserOutput execute(final CreateUserCommand command) {
		if (userRepository.findByEmailIgnoreCase(command.email()).isPresent()) {
			throw new DomainException("Conflict", "Email already exists", 409);
		}
		if (userRepository.findByLoginIgnoreCase(command.login()).isPresent()) {
			throw new DomainException("Conflict", "Login already exists", 409);
		}

		final var now = Instant.now();
		final User user = UserFactory.create(
				null,
				null,
				command.type(),
				command.name(),
				command.email(),
				command.login(),
				true,
				encryptionService.encode(command.password()),
				new Address(command.street(), command.number(), command.city(), command.zipCode()),
				now,
				now
		);

		final UserJpaEntity saved = userRepository.save(userPersistenceMapper.fromDomain(user));
		return new CreateUserOutput(userPersistenceMapper.toDomain(saved));
	}
}
