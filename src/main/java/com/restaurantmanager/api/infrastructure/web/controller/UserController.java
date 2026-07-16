package com.restaurantmanager.api.infrastructure.web.controller;

import com.restaurantmanager.api.UsersApi;
import com.restaurantmanager.api.model.CreateUserRequest;
import com.restaurantmanager.api.model.UpdateUserRequest;
import com.restaurantmanager.api.model.UserResponse;
import com.restaurantmanager.api.application.usecase.user.create.CreateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.get.GetUserUseCase;
import com.restaurantmanager.api.application.usecase.user.list.ListUserCase;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserUseCase;
import com.restaurantmanager.api.infrastructure.web.mapper.UserMapper;
import com.restaurantmanager.api.domain.model.Pagination;
import com.restaurantmanager.api.domain.model.User;
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
    private final GetUserUseCase getUserByUuidUseCase;
    private final ListUserCase listUserCase;
    private final UpdateUserUseCase updateUserByUuidUseCase;
    private final DeleteUserUseCase deleteUserByUuidUseCase;
    private final UserMapper userMapper;

    public UserController(
            final CreateUserUseCase createUserUseCase,
            final GetUserUseCase getUserByUuidUseCase,
            final ListUserCase listUserCase,
            final UpdateUserUseCase updateUserByUuidUseCase,
            final DeleteUserUseCase deleteUserByUuidUseCase,
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
        final java.util.UUID typeUuid = createUserRequest.getType().getId();

        final User user = new User(
            null,
            null,
            createUserRequest.getName(),
            createUserRequest.getEmail(),
            createUserRequest.getLogin(),
            true,
            null,
            null,
            null
        );

        final User created = createUserUseCase.execute(typeUuid, user);
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
        final var pagination = new Pagination(page, size, "name");
        final var pageResult = listUserCase.execute(pagination);
        final List<UserResponse> content = pageResult.getContent().stream().map(userMapper::map).toList();
        return ResponseEntity.ok(content);
    }

    @Override
    public ResponseEntity<UserResponse> updateUser(UUID userId, @Valid UpdateUserRequest updateUserRequest) {
        final User user = new User(
            null,
            null,
            updateUserRequest.getName(),
            updateUserRequest.getEmail(),
            null,
            null,
            null,
            null
        );
        final java.util.UUID typeUuid = updateUserRequest.getType() != null ? updateUserRequest.getType().getId() : null;

        final User updated = updateUserByUuidUseCase.execute(userId, user, typeUuid);
        return ResponseEntity.ok(userMapper.map(updated));
    }
}

