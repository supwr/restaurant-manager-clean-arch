package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.persistence.entity.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemPersistenceMapper {

    default MenuItem toDomain(final MenuItemEntity entity) {
        if (entity == null) {
            return null;
        }
        return new MenuItem(
            entity.getId(),
            entity.getUuid(),
            entity.getRestaurantId(),
            entity.getName(),
            entity.getDescription(),
            entity.getPrice(),
            entity.getLocalOnly(),
            entity.getPhotoPath()
        );
    }

    default MenuItemEntity toEntity(final MenuItem domain) {
        if (domain == null) {
            return null;
        }
        final MenuItemEntity entity = new MenuItemEntity();
        entity.setId(domain.getId());
        entity.setUuid(domain.getUuid());
        entity.setRestaurantId(domain.getRestaurantId());
        entity.setName(domain.getName());
        entity.setDescription(domain.getDescription());
        entity.setPrice(domain.getPrice());
        entity.setLocalOnly(domain.getLocalOnly());
        entity.setPhotoPath(domain.getPhotoPath());
        return entity;
    }
}

