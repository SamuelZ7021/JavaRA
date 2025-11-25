package com.Events.events.infrastructure.config;

import com.Events.events.application.usecase.EventUseCaseImpl;
import com.Events.events.application.usecase.UserUseCaseImpl;
import com.Events.events.application.usecase.VenueUseCaseImpl;
import com.Events.events.domain.ports.in.EventUseCase;
import com.Events.events.domain.ports.in.UserUseCase;
import com.Events.events.domain.ports.in.VenueUseCase;
import com.Events.events.domain.ports.out.EventRepositoryPort;
import com.Events.events.domain.ports.out.UserRepositoryPort;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VenueUseCase venueUseCase(VenueRepositoryPort venueRepositoryPort) {
        return new VenueUseCaseImpl(venueRepositoryPort);
    }

    @Bean
    public EventUseCase eventUseCase(EventRepositoryPort eventRepositoryPort, VenueRepositoryPort venueRepositoryPort) {
        return new EventUseCaseImpl(eventRepositoryPort, venueRepositoryPort);
    }

    // Agregamos el Bean de Usuario
    @Bean
    public UserUseCase userUseCase(UserRepositoryPort userRepositoryPort) {
        return new UserUseCaseImpl(userRepositoryPort);
    }
}