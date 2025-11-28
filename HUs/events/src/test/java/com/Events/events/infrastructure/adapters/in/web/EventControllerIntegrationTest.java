package com.Events.events.infrastructure.adapters.in.web;

import com.Events.events.EventsApplication;
import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.infrastructure.adapters.in.web.dto.resquest.CreateEventRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = EventsApplication.class // <--- Le decimos explícitamente dónde arrancar
)
@AutoConfigureMockMvc
@Testcontainers // Habilita el soporte para contenedores
class EventControllerIntegrationTest {

    // Levantamos un MySQL real en Docker para la prueba
    @Container
    @ServiceConnection // Configura automáticamente spring.datasource.url, username, etc.
    static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private VenueRepositoryPort venueRepositoryPort;

    @BeforeEach
    void setUp() {
        // Pre-cargamos datos necesarios en la BD real (del contenedor)
        // Necesitamos un Venue para poder crear un Evento
        Venue venue = new Venue(null, "Test Arena", "Medellin");
        venueRepositoryPort.save(venue);
    }

    @Test
    @WithMockUser(username = "samuel@test.com", roles = {"USER"}) // Bypassea JWT simulando usuario logueado
    void shouldCreateEventE2E() throws Exception {
        // Buscamos el ID del venue que acabamos de guardar (será 1 probablemente)
        Long venueId = venueRepositoryPort.findAll().get(0).getId();

        CreateEventRequest request = new CreateEventRequest();
        request.setName("Integration Test Concert");
        request.setCategory("Rock");
        request.setStartDate(LocalDateTime.now().plusDays(10));
        request.setEndDate(LocalDateTime.now().plusDays(10).plusHours(2));
        request.setVenueId(venueId);

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated()) // Esperamos 201 Created
                .andExpect(jsonPath("$.name").value("Integration Test Concert"))
                .andExpect(jsonPath("$.venueName").value("Test Arena"));
    }

    @Test
    void shouldFailCreateEventWhenNotAuthenticated() throws Exception {
        // Probamos que la seguridad funciona: Sin @WithMockUser debe fallar
        CreateEventRequest request = new CreateEventRequest();
        // ... setear datos ...

        mockMvc.perform(post("/api/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden()); // O isUnauthorized 403/401
    }
}