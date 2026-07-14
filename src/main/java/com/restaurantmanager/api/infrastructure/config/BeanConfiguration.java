package com.restaurantmanager.api.infrastructure.config;

import com.restaurantmanager.api.application.gateway.MenuItemGateway;
import com.restaurantmanager.api.application.gateway.RestaurantGateway;
import com.restaurantmanager.api.application.gateway.UserGateway;
import com.restaurantmanager.api.application.gateway.UserTypeGateway;
import com.restaurantmanager.api.application.usecase.menuitem.create.CreateMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.delete.DeleteMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.get.GetMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.list.ListMenuItemsUseCase;
import com.restaurantmanager.api.application.usecase.menuitem.update.UpdateMenuItemUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.create.CreateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.delete.DeleteRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.get.GetRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.list.ListRestaurantsUseCase;
import com.restaurantmanager.api.application.usecase.restaurant.update.UpdateRestaurantUseCase;
import com.restaurantmanager.api.application.usecase.user.create.CreateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserUseCase;
import com.restaurantmanager.api.application.usecase.user.delete.DeleteUserByUuidUseCase;
import com.restaurantmanager.api.application.usecase.user.get.GetUserUseCase;
import com.restaurantmanager.api.application.usecase.user.get.GetUserByUuidUseCase;
import com.restaurantmanager.api.application.usecase.user.list.ListUserCase;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserUseCase;
import com.restaurantmanager.api.application.usecase.user.update.UpdateUserByUuidUseCase;
import com.restaurantmanager.api.application.usecase.usertype.create.CreateUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.delete.DeleteUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.get.GetUserTypeUseCase;
import com.restaurantmanager.api.application.usecase.usertype.list.ListUserTypesUseCase;
import com.restaurantmanager.api.application.usecase.usertype.update.UpdateUserTypeUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Central bean configuration for application use cases. Each use case is exposed as a Spring @Bean so
 * infrastructure adapters (controllers, services, repositories) can inject them as needed.
 */
@Configuration
public class BeanConfiguration {

    // --- User use cases -------------------------------------------------
    @Bean
    public CreateUserUseCase createUserUseCase(final UserGateway userGateway) {
        return new CreateUserUseCase(userGateway);
    }

    @Bean
    public GetUserUseCase getUserUseCase(final UserGateway userGateway) {
        return new GetUserUseCase(userGateway);
    }

    @Bean
    public ListUserCase listUserCase(final UserGateway userGateway) {
        return new ListUserCase(userGateway);
    }

    @Bean
    public UpdateUserUseCase updateUserUseCase(final UserGateway userGateway) {
        return new UpdateUserUseCase(userGateway);
    }

    @Bean
    public DeleteUserUseCase deleteUserUseCase(final UserGateway userGateway) {
        return new DeleteUserUseCase(userGateway);
    }

    @Bean
    public GetUserByUuidUseCase getUserByUuidUseCase(final UserGateway userGateway) {
        return new GetUserByUuidUseCase(userGateway);
    }

    @Bean
    public DeleteUserByUuidUseCase deleteUserByUuidUseCase(final UserGateway userGateway) {
        return new DeleteUserByUuidUseCase(userGateway);
    }

    @Bean
    public UpdateUserByUuidUseCase updateUserByUuidUseCase(final UserGateway userGateway) {
        return new UpdateUserByUuidUseCase(userGateway);
    }

    // --- Restaurant use cases -------------------------------------------
    @Bean
    public CreateRestaurantUseCase createRestaurantUseCase(final RestaurantGateway restaurantGateway) {
        return new CreateRestaurantUseCase(restaurantGateway);
    }

    @Bean
    public ListRestaurantsUseCase listRestaurantsUseCase(final RestaurantGateway restaurantGateway) {
        return new ListRestaurantsUseCase(restaurantGateway);
    }

    @Bean
    public GetRestaurantUseCase getRestaurantUseCase(final RestaurantGateway restaurantGateway) {
        return new GetRestaurantUseCase(restaurantGateway);
    }

    @Bean
    public UpdateRestaurantUseCase updateRestaurantUseCase(final RestaurantGateway restaurantGateway) {
        return new UpdateRestaurantUseCase(restaurantGateway);
    }

    @Bean
    public DeleteRestaurantUseCase deleteRestaurantUseCase(final RestaurantGateway restaurantGateway) {
        return new DeleteRestaurantUseCase(restaurantGateway);
    }

    // --- MenuItem use cases ---------------------------------------------
    @Bean
    public CreateMenuItemUseCase createMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
        return new CreateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public ListMenuItemsUseCase listMenuItemsUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
        return new ListMenuItemsUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public GetMenuItemUseCase getMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
        return new GetMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public DeleteMenuItemUseCase deleteMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
        return new DeleteMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    @Bean
    public UpdateMenuItemUseCase updateMenuItemUseCase(final MenuItemGateway menuItemGateway, final RestaurantGateway restaurantGateway) {
        return new UpdateMenuItemUseCase(menuItemGateway, restaurantGateway);
    }

    // --- UserType use cases --------------------------------------------
    @Bean
    public CreateUserTypeUseCase createUserTypeUseCase(final UserTypeGateway userTypeGateway) {
        return new CreateUserTypeUseCase(userTypeGateway);
    }

    @Bean
    public ListUserTypesUseCase listUserTypesUseCase(final UserTypeGateway userTypeGateway) {
        return new ListUserTypesUseCase(userTypeGateway);
    }

    @Bean
    public GetUserTypeUseCase getUserTypeUseCase(final UserTypeGateway userTypeGateway) {
        return new GetUserTypeUseCase(userTypeGateway);
    }

    @Bean
    public UpdateUserTypeUseCase updateUserTypeUseCase(final UserTypeGateway userTypeGateway) {
        return new UpdateUserTypeUseCase(userTypeGateway);
    }

    @Bean
    public DeleteUserTypeUseCase deleteUserTypeUseCase(final UserTypeGateway userTypeGateway) {
        return new DeleteUserTypeUseCase(userTypeGateway);
    }
}

