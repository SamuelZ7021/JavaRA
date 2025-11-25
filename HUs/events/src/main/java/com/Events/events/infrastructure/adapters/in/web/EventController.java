package com.Events.events.infrastructure.adapters.in.web;

import com.Events.events.infrastructure.adapters.in.web.dto.response.EventResponse;
import com.Events.events.infrastructure.adapters.in.web.dto.resquest.CreateEventRequest;
import com.Events.events.domain.model.Event;
import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.in.EventUseCase;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "Operaciones relacionadas con la gestión de eventos")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    // Endpoint básico (Mantenemos por compatibilidad o lo reemplazamos si quieres)
    @Operation(summary = "Listar todos los eventos (Sin paginación)", description = "Retorna la lista completa.")
    @GetMapping("/all")
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> response = eventUseCase.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    // NUEVO ENDPOINT PAGINADO
    @Operation(summary = "Listar eventos con Paginación y Filtros",
            description = "Permite filtrar por ciudad o categoría y paginar los resultados.")
    @GetMapping
    public ResponseEntity<List<EventResponse>> getEventsPaginated(
            @Parameter(description = "Número de página (empieza en 0)")
            @RequestParam(defaultValue = "0") int page,

            @Parameter(description = "Tamaño de la página")
            @RequestParam(defaultValue = "10") int size,

            @Parameter(description = "Filtrar por ciudad del Venue")
            @RequestParam(required = false) String city,

            @Parameter(description = "Filtrar por categoría del evento")
            @RequestParam(required = false) String category
    ) {
        List<EventResponse> response = eventUseCase.findByFilter(page, size, city, category)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Buscar evento por ID", description = "Retorna un único evento basado en su identificador.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EventResponse> getEventById(@PathVariable Long id) {
        return ResponseEntity.ok(toResponse(eventUseCase.findById(id)));
    }

    @Operation(summary = "Crear un nuevo evento")
    @PostMapping
    public ResponseEntity<EventResponse> createEvent(@RequestBody @Valid CreateEventRequest request) {
        Venue venueDummy = new Venue(request.getVenueId(), null, null);
        Event eventDomain = new Event(null, request.getName(), request.getCategory(), request.getStartDate(), venueDummy);
        Event createdEvent = eventUseCase.create(eventDomain);
        return new ResponseEntity<>(toResponse(createdEvent), HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar evento")
    @PutMapping("/{id}")
    public ResponseEntity<EventResponse> updateEvent(@PathVariable Long id, @RequestBody @Valid CreateEventRequest request) {
        Venue venueDummy = new Venue(request.getVenueId(), null, null);
        Event eventDomain = new Event(id, request.getName(), request.getCategory(), request.getStartDate(), venueDummy);
        return ResponseEntity.ok(toResponse(eventUseCase.update(id, eventDomain)));
    }

    @Operation(summary = "Eliminar evento")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        eventUseCase.delete(id);
        return ResponseEntity.noContent().build();
    }

    private EventResponse toResponse(Event event) {
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getCategory(),
                event.getStartDate(),
                event.getVenue() != null ? event.getVenue().getName() : "Sin Lugar"
        );
    }
}