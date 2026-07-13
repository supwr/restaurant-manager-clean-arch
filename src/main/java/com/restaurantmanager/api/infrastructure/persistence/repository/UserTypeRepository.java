package com.restaurantmanager.api.infrastructure.persistence.repository;

import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTypeRepository extends JpaRepository<UserTypeEntity, Long> {

    Optional<UserTypeEntity> findByName(String name);

    boolean existsByName(String name);
}

