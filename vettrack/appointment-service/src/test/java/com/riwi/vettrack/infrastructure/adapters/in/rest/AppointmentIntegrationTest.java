package com.riwi.vettrack.infrastructure.adapters.in.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VetAvailabilityPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.AppointmentRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.VeterinarianEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.VeterinarianJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.*;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers // Activa Testcontainers
class AppointmentIntegrationTest {

    @Container
    @ServiceConnection // Configura automágicamente el DataSource
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private VeterinarianJpaRepository vetRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean // Mockeamos el servicio externo para no depender de que esté levantado
    private VetAvailabilityPort vetAvailabilityPort;

    @BeforeEach
    void setUp() {
        vetRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"}) // Simulamos usuario autenticado
    void shouldCreateAppointmentSuccessfully() throws Exception {
        // GIVEN
        VeterinarianEntity vet = new VeterinarianEntity(null, "Dr. Integration", "test@test.com", "123", true);
        vet = vetRepository.save(vet); // Guardamos en la BD real del contenedor

        // Simulamos que el servicio externo dice "Disponible"
        Mockito.when(vetAvailabilityPort.checkAvailability(Mockito.any(), Mockito.any()))
                .thenReturn(new VetAvailabilityPort.AvailabilityResponse(true, "Available"));

        AppointmentRequest request = new AppointmentRequest(
                100L,
                vet.getId(),
                LocalDateTime.now().plusDays(1),
                "Test Integration"
        );

        // WHEN & THEN
        mockMvc.perform(post("/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists()) // ¡Debe tener ID!
                .andExpect(jsonPath("$.status").value("CONFIRMADA"));
    }
}