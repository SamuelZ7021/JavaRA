package com.Events.events.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Events Management API (Hexagonal Architecture)",
                version = "1.0.0",
                description = "API para la gestión de Eventos y Lugares usando Ports & Adapters.",
                contact = @Contact(
                        name = "Tu Nombre / ArcJava Team",
                        email = "dev@riwi.io"
                ),
                license = @License(name = "MIT", url = "https://opensource.org/licenses/MIT")
        )
)
public class OpenApiConfig {
}