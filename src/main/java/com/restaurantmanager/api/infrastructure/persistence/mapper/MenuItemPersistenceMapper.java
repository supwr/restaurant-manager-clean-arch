package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.persistence.entity.MenuItemEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemPersistenceMapper {

    MenuItem toDomain(MenuItemEntity entity);

    MenuItemEntity toEntity(MenuItem domain);
}

