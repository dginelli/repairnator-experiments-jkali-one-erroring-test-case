package com.kh013j;

import com.kh013j.model.service.*;
import com.kh013j.model.service.interfaces.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CaffinoApplicationConfiguration {
    @Bean
    public DishService dishServiceConfig() {
        return new DishServiceImpl();
    }

    @Bean
    public OrderService orderServiceConfig() {
        return new OrderServiceImpl();
    }

    @Bean
    public UserService userServiceConfig() {
        return new UserServiceImpl();
    }

    @Bean
    public RoleService roleServiceConfig() {
        return new RoleServiceImpl();
    }

    @Bean
    public StatusService statusServiceConfig() {
        return new StatusServiseImpl();
    }

    @Bean
    public OrderedDishService orderedDishServiceConfig() {
        return new OrderedDishServiceImpl();
    }

    @Bean
    public CategoryService categoryServiceConfig() {
        return new CategoryServiceImpl();
    }
}