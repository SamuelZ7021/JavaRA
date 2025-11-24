package com.Events.events.infrastructure.config;

import com.Events.events.application.usecase.UserUseCaseImpl;
import com.Events.events.domain.ports.out.UserRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    // Esto permite que la clase CreateUserUseCaseImpl permanezca limpia de anotaciones @Service
    @Bean
    public UserUseCaseImpl userUseCase(UserRepositoryPort userRepositoryPort){
        return new UserUseCaseImpl(userRepositoryPort);
    }

}
