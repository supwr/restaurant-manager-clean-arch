package com.restaurantmanager.api.infrastructure.web.mapper;

import com.restaurantmanager.api.domain.model.UserType;
import com.restaurantmanager.api.model.UserTypeRequest;
import com.restaurantmanager.api.model.UserTypeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserTypeMapper {

    UserTypeResponse map(UserType userType);

    default UserType map(final UserTypeRequest request) {
        if (request == null) {
            return null;
        }
        return new UserType(null, request.getName());
    }

    default UserType map(final Long id, final UserTypeRequest request) {
        if (request == null) {
            return null;
        }
        return new UserType(id, request.getName());
    }
}

