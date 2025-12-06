package com.riwi.vettrack;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers // <--- Activamos Testcontainers
class VettrackApplicationTests {

    // Definimos la BD containerizada igual que en el test de integración
    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Test
    void contextLoads() {
        // Este test solo verifica que Spring Boot pueda arrancar sin explotar.
        // Al tener postgres containerizado, ahora pasará exitosamente.
    }

}