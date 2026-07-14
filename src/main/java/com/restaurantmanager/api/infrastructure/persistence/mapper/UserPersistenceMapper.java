package com.restaurantmanager.api.infrastructure.persistence.mapper;

import com.restaurantmanager.api.domain.model.Customer;
import com.restaurantmanager.api.domain.model.Owner;
import com.restaurantmanager.api.domain.model.User;
import com.restaurantmanager.api.infrastructure.persistence.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserPersistenceMapper {

    @Mapping(target = "typeId", expression = "java(resolveTypeId(user))")
    UserEntity toEntity(User user);

    default Long resolveTypeId(final User user) {
        if (user == null) {
            return 1L;
        }
        if (user instanceof Owner) {
            return 1L;
        }
        if (user instanceof Customer) {
            return 2L;
        }
        return 1L;
    }

    default User toDomain(final UserEntity entity) {
        if (entity == null) {
            return null;
        }
        final int type = entity.getTypeId() == null ? 1 : entity.getTypeId().intValue();
        if (type == 2) {
            return com.restaurantmanager.api.domain.model.Customer.create(
                entity.getId(),
                entity.getUuid(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getActive(),
                null,
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
            );
        } else {
            return com.restaurantmanager.api.domain.model.Owner.create(
                entity.getId(),
                entity.getUuid(),
                entity.getName(),
                entity.getEmail(),
                entity.getLogin(),
                entity.getActive(),
                null,
                null,
                entity.getCreatedAt(),
                entity.getUpdatedAt()
            );
        }
    }
}

