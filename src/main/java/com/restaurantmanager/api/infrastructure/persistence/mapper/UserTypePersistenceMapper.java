package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.infrastructure.persistence.entity.UserTypeEntity;
import com.restaurantmanager.api.domain.model.UserType;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserTypePersistenceMapper {

    default UserType toDomain(final UserTypeEntity entity) {
        if (entity == null) {
            return null;
        }
        return new UserType(entity.getId(), entity.getName(), entity.getObservation());
    }

    default UserTypeEntity toEntity(final UserType domain) {
        if (domain == null) {
            return null;
        }
        final UserTypeEntity entity = new UserTypeEntity();
        entity.setId(domain.getId());
        entity.setName(domain.getName());
        entity.setObservation(domain.getObservation());
        return entity;
    }
}

