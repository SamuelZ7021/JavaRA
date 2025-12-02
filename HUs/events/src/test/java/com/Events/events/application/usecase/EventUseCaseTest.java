package com.Events.events.application.usecase;

import com.Events.events.domain.model.Event;
import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.out.EventRepositoryPort;
import com.Events.events.domain.ports.out.VenueRepositoryPort;
import com.Events.events.exception.DuplicateEventException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// @ExtendWith: Inicializa los mocks sin necesidad de levantar el contexto de Spring (Rápido)
@ExtendWith(MockitoExtension.class)
class EventUseCaseTest {

    // @Mock: Crea objetos falsos de las dependencias
    @Mock
    private EventRepositoryPort eventRepositoryPort;

    @Mock
    private VenueRepositoryPort venueRepositoryPort;

    // @InjectMocks: Inyecta los mocks dentro de nuestra clase a probar
    @InjectMocks
    private EventUseCaseImpl eventUseCase;

    @Test
    @DisplayName("Should create event successfully when venue exists and name is unique")
    void shouldCreateEventSuccessfully() {
        // 1. ARRANGE (Preparar)
        Long venueId = 1L;
        Venue venue = new Venue(venueId, "Movistar Arena", "Bogota");
        Event eventToCreate = new Event(null, "Concierto Rock", "Music", LocalDateTime.now().plusDays(1), venue);

        // Simulamos que el nombre NO existe
        when(eventRepositoryPort.existsByName(eventToCreate.getName())).thenReturn(false);
        // Simulamos que el Venue SI existe (recuperando el objeto completo)
        when(venueRepositoryPort.findById(venueId)).thenReturn(Optional.of(venue));
        // Simulamos el guardado (devuelve el mismo evento con ID asignado)
        when(eventRepositoryPort.save(any(Event.class))).thenAnswer(invocation -> {
            Event e = invocation.getArgument(0);
            e.setId(100L);
            return e;
        });

        // 2. ACT (Actuar)
        Event createdEvent = eventUseCase.create(eventToCreate);

        // 3. ASSERT (Verificar)
        assertNotNull(createdEvent.getId());
        assertEquals("Movistar Arena", createdEvent.getVenue().getName());

        // Verificamos que se llamaron los métodos esperados
        verify(eventRepositoryPort).existsByName("Concierto Rock");
        verify(venueRepositoryPort).findById(venueId);
        verify(eventRepositoryPort).save(any(Event.class));
    }

    @Test
    @DisplayName("Should throw DuplicateEventException when event name already exists")
    void shouldThrowExceptionWhenNameExists() {
        // 1. ARRANGE
        Venue venue = new Venue(1L, "Dummy", "City");
        Event eventToCreate = new Event(null, "Existing Event", "Music", LocalDateTime.now(), venue);

        when(eventRepositoryPort.existsByName("Existing Event")).thenReturn(true);

        // 2. & 3. ACT & ASSERT
        assertThrows(DuplicateEventException.class, () -> {
            eventUseCase.create(eventToCreate);
        });

        // Verificamos que NUNCA se intentó guardar
        verify(eventRepositoryPort, never()).save(any(Event.class));
    }
}