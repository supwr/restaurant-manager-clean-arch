package com.restaurantmanager.api.infrastructure.persistence.gateway;

import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import com.restaurantmanager.api.infrastructure.persistence.mapper.UserPersistenceMapper;
import com.restaurantmanager.api.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserPersistenceGatewayTest {

    @Mock
    private UserRepository userRepository;

    private UserPersistenceMapper userPersistenceMapper;
    private UserPersistenceGateway gateway;

    @BeforeEach
    void setUp() {
        userPersistenceMapper = Mappers.getMapper(UserPersistenceMapper.class);
        gateway = new UserPersistenceGateway(userRepository, userPersistenceMapper);
    }

    @Test
    void testSaveOwner() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();
        User owner = Owner.create(null, uuid, "John Doe", "john@example.com", "john.doe", true, null, null, now, now);

        UserEntity savedEntity = new UserEntity();
        savedEntity.setId(1L);
        savedEntity.setUuid(uuid);
        savedEntity.setName("John Doe");
        savedEntity.setEmail("john@example.com");
        savedEntity.setLogin("john.doe");
        savedEntity.setActive(true);
        savedEntity.setTypeId(1L);
        savedEntity.setCreatedAt(now);
        savedEntity.setUpdatedAt(now);

        when(userRepository.save(any(UserEntity.class))).thenReturn(savedEntity);

        User result = gateway.save(owner);

        assertNotNull(result);
        assertInstanceOf(Owner.class, result);
        assertEquals(1L, result.getId());
        assertEquals(uuid, result.getUuid());
        assertEquals("John Doe", result.getName());

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    void testFindByUuid() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("John Doe");
        entity.setEmail("john@example.com");
        entity.setLogin("john.doe");
        entity.setActive(true);
        entity.setTypeId(1L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        when(userRepository.findByUuid(uuid)).thenReturn(Optional.of(entity));

        Optional<User> result = gateway.findByUuid(uuid);

        assertTrue(result.isPresent());
        assertInstanceOf(Owner.class, result.get());
        assertEquals("John Doe", result.get().getName());

        verify(userRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testFindByUuidNotFound() {
        UUID uuid = UUID.randomUUID();
        when(userRepository.findByUuid(uuid)).thenReturn(Optional.empty());

        Optional<User> result = gateway.findByUuid(uuid);

        assertFalse(result.isPresent());
        verify(userRepository, times(1)).findByUuid(uuid);
    }

    @Test
    void testFindById() {
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        UserEntity entity = new UserEntity();
        entity.setId(1L);
        entity.setUuid(uuid);
        entity.setName("John Doe");
        entity.setEmail("john@example.com");
        entity.setLogin("john.doe");
        entity.setActive(true);
        entity.setTypeId(1L);
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        when(userRepository.findById(1L)).thenReturn(Optional.of(entity));

        Optional<User> result = gateway.findById(1L);

        assertTrue(result.isPresent());
        assertEquals("John Doe", result.get().getName());

        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void testDeleteById() {
        gateway.deleteById(1L);

        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testExistsById() {
        when(userRepository.existsById(1L)).thenReturn(true);

        boolean result = gateway.existsById(1L);

        assertTrue(result);
        verify(userRepository, times(1)).existsById(1L);
    }
}

