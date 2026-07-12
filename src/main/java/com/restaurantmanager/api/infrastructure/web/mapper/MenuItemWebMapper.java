package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.MenuItem;
import com.restaurantmanager.api.infrastructure.web.dto.MenuItemDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MenuItemWebMapper {

    MenuItem toDomain(MenuItemDTO dto);

    MenuItemDTO toDTO(MenuItem domain);
}

