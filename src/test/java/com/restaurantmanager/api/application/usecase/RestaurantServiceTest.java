package com.restaurantmanager.api.application.usecase;

import com.restaurantmanager.api.domain.exception.EntityNotFoundException;
import com.restaurantmanager.api.domain.model.Restaurant;
import com.restaurantmanager.api.application.port.RestaurantPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestaurantPersistencePort persistencePort;

    @InjectMocks
    private RestaurantService service;

    private Restaurant valid;

    @BeforeEach
    void setUp() {
        valid = Restaurant.builder()
            .name("Pizza House")
            .address("123")
            .cuisineType("Italian")
            .openingHours("9-22")
            .ownerUserId(1L)
            .build();
    }

    @Test
    void createSuccess() {
        when(persistencePort.save(any(Restaurant.class))).thenReturn(Restaurant.builder().id(1L).name(valid.getName()).build());

        var created = service.create(valid);

        assertNotNull(created);
        assertEquals(1L, created.getId());
        verify(persistencePort, times(1)).save(any());
    }

    @Test
    void getByIdSuccess() {
        when(persistencePort.findById(1L)).thenReturn(Optional.of(Restaurant.builder().id(1L).name("X").build()));
        var r = service.getById(1L);
        assertEquals(1L, r.getId());
    }

    @Test
    void getByIdNotFound() {
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.getById(999L));
    }

    @Test
    void listAll() {
        List<Restaurant> list = Arrays.asList(Restaurant.builder().id(1L).name("A").build());
        when(persistencePort.findAll()).thenReturn(list);
        var result = service.listAll();
        assertEquals(1, result.size());
    }

    @Test
    void updateSuccess() {
        Restaurant existing = Restaurant.builder().id(1L).name("Old").address("a").cuisineType("c").openingHours("h").ownerUserId(1L).build();
        Restaurant updated = Restaurant.builder().name("New").address("new").cuisineType("c").openingHours("h").ownerUserId(1L).build();

        when(persistencePort.findById(1L)).thenReturn(Optional.of(existing));
        when(persistencePort.save(any(Restaurant.class))).thenReturn(Restaurant.builder().id(1L).name("New").build());

        var r = service.update(1L, updated);
        assertEquals("New", r.getName());
    }

    @Test
    void updateNotFound() {
        when(persistencePort.findById(999L)).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> service.update(999L, valid));
    }

    @Test
    void deleteSuccess() {
        when(persistencePort.existsById(1L)).thenReturn(true);
        assertDoesNotThrow(() -> service.delete(1L));
        verify(persistencePort, times(1)).deleteById(1L);
    }

    @Test
    void deleteNotFound() {
        when(persistencePort.existsById(999L)).thenReturn(false);
        assertThrows(EntityNotFoundException.class, () -> service.delete(999L));
    }
}

