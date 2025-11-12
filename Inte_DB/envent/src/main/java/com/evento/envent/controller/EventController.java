package com.evento.envent.controller;

import com.evento.envent.controller.dto.CreateEventRequest;
import com.evento.envent.controller.dto.EventResponse;
import com.evento.envent.services.EventServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Gestión de Eventos", description = "API para el CRUD de Eventos (Versión en Memoria H1)")
public class EventController {

    private final EventServices eventService;

    public EventController(EventServices eventService) {
        this.eventService = eventService;
    }

    @Operation(summary = "Obtener todos los eventos", description = "Retorna una lista de todos los eventos en memoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de eventos obtenida exitosamente")
    })
    @GetMapping
    public List<EventResponse> getAllEvents() {
        return eventService.findAll();
    }

    @Operation(summary = "Obtener un evento por ID", description = "Retorna un evento específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento encontrado"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public EventResponse getEventById(@PathVariable Long id) {
        return eventService.findById(id);
    }

    @Operation(summary = "Crear un nuevo evento", description = "Crea un nuevo evento y lo almacena en memoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Evento creado exitosamente",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos (ej. campos vacíos)", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@RequestBody @Valid CreateEventRequest request) {
        return eventService.createEvent(request);
    }

    @Operation(summary = "Actualizar un evento existente", description = "Actualiza un evento por su ID con la información proporcionada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Evento actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = EventResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public EventResponse updateEvent(@PathVariable Long id,
                                     @RequestBody @Valid CreateEventRequest request) {
        return eventService.updateEvent(id, request);
    }

    @Operation(summary = "Eliminar un evento", description = "Elimina un evento existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Evento eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Evento no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // 204
    public void deleteEvent(@PathVariable Long id) {
        eventService.deleteEvent(id);
    }
}