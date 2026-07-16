package com.restaurantmanager.api.infrastructure.persistence.repository;

import com.restaurantmanager.api.infrastructure.persistence.entity.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItemEntity, Long> {

    @Query("select m from MenuItemEntity m where m.restaurant.id = :restaurantId")
    List<MenuItemEntity> findByRestaurantId(Long restaurantId);

    Optional<MenuItemEntity> findByUuid(UUID uuid);

    boolean existsByUuid(UUID uuid);

    void deleteByUuid(UUID uuid);
}

