package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.UsersApi;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.UpdateUserRequest;
import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.application.usecase.user.create.CreateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.get.GetUserByUuidUseCase;
import com.restaurantmanager.api.application.usecase.user.list.ListUserCase;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserByUuidUseCase;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserByUuidUseCase;
import com.restaurantmanager.api.infrastructure.web.mapper.UserMapper;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.domain.model.Address;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.Customer;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
public class UserController implements UsersApi {

    private final CreateUserUseCase createUserUseCase;
    private final GetUserByUuidUseCase getUserByUuidUseCase;
    private final ListUserCase listUserCase;
    private final UpdateUserByUuidUseCase updateUserByUuidUseCase;
    private final DeleteUserByUuidUseCase deleteUserByUuidUseCase;
    private final UserMapper userMapper;

    public UserController(
            final CreateUserUseCase createUserUseCase,
            final GetUserByUuidUseCase getUserByUuidUseCase,
            final ListUserCase listUserCase,
            final UpdateUserByUuidUseCase updateUserByUuidUseCase,
            final DeleteUserByUuidUseCase deleteUserByUuidUseCase,
            final UserMapper userMapper
    ) {
        this.createUserUseCase = Objects.requireNonNull(createUserUseCase);
        this.getUserByUuidUseCase = Objects.requireNonNull(getUserByUuidUseCase);
        this.listUserCase = Objects.requireNonNull(listUserCase);
        this.updateUserByUuidUseCase = Objects.requireNonNull(updateUserByUuidUseCase);
        this.deleteUserByUuidUseCase = Objects.requireNonNull(deleteUserByUuidUseCase);
        this.userMapper = Objects.requireNonNull(userMapper);
    }

    @Override
    public ResponseEntity<UserResponse> createUser(@Valid CreateUserRequest createUserRequest) {
        final User user = toDomain(createUserRequest);

        final User created = createUserUseCase.execute(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.map(created));
    }

    @Override
    public ResponseEntity<Void> deleteUser(UUID userId) {
        deleteUserByUuidUseCase.execute(userId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<UserResponse> getUserById(UUID userId) {
        final User user = getUserByUuidUseCase.execute(userId);
        return ResponseEntity.ok(userMapper.map(user));
    }

    @Override
    public ResponseEntity<List<UserResponse>> listUsers(String name, Integer page, Integer size) {
        final int p = page == null ? 0 : page;
        final int s = size == null ? 20 : size;
        final Pagination pagination = new Pagination(p, s, "id");
        final var pageResult = listUserCase.execute(pagination);
        final List<UserResponse> content = pageResult.getContent().stream().map(userMapper::map).toList();
        return ResponseEntity.ok(content);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, @Valid UpdateUserRequest updateUserRequest) {
        final User user = new MutableUser(
            null,
            null,
            updateUserRequest.getName(),
            updateUserRequest.getEmail(),
            null,
            null,
            null,
            null,
            null,
            null
        );

        final User updated = updateUserByUuidUseCase.execute(userId, user);
        return ResponseEntity.ok(userMapper.map(updated));
    }

    private User toDomain(final CreateUserRequest request) {
        final String typeName = request.getType() == null ? null : request.getType().getName();
        final User user = User.CUSTOMER_TYPE.equalsIgnoreCase(typeName)
            ? Customer.create(null, null, request.getName(), request.getEmail(), request.getLogin(), true, null, null, null, null)
            : Owner.create(null, null, request.getName(), request.getEmail(), request.getLogin(), true, null, null, null, null);

        return user;
    }


    private static final class MutableUser extends User {
        private MutableUser(
            final Long id,
            final java.util.UUID uuid,
            final String name,
            final String email,
            final String login,
            final Boolean active,
            final String password,
            final Address address,
            final java.time.Instant createdAt,
            final java.time.Instant updatedAt
        ) {
            super(id, uuid, name, email, login, active, password, address, createdAt, updatedAt);
        }
    }
}

