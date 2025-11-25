package com.Events.events.infrastructure.adapters.in.web;

import com.Events.events.infrastructure.adapters.in.web.dto.resquest.CreateVenueRequest;
import com.Events.events.infrastructure.adapters.in.web.dto.response.VenueResponse;
import com.Events.events.domain.model.Venue;
import com.Events.events.domain.ports.in.VenueUseCase;

// Imports de Swagger
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
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/venues")
@Tag(name = "Lugares (Venues)", description = "Administración de espacios físicos para eventos")
public class VenueController {

    private final VenueUseCase venueUseCase;

    public VenueController(VenueUseCase venueUseCase) {
        this.venueUseCase = venueUseCase;
    }

    @Operation(summary = "Listar lugares", description = "Obtiene todos los lugares registrados en la base de datos.")
    @ApiResponse(responseCode = "200", description = "Lista obtenida exitosamente")
    @GetMapping
    public List<VenueResponse> getAllVenues() {
        return venueUseCase.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Operation(summary = "Buscar lugar por ID", description = "Recupera la información detallada de un lugar específico.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public VenueResponse getVenueById(@PathVariable Long id) {
        return toResponse(venueUseCase.findById(id));
    }

    @Operation(summary = "Registrar nuevo lugar", description = "Crea un nuevo espacio físico en el sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos inválidos (ej. nombre vacío)", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VenueResponse createVenue(@RequestBody @Valid CreateVenueRequest request) {
        Venue venueDomain = new Venue(null, request.getName(), request.getCity());
        Venue savedVenue = venueUseCase.create(venueDomain);
        return toResponse(savedVenue);
    }

    @Operation(summary = "Actualizar lugar", description = "Modifica el nombre o ciudad de un lugar existente.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lugar actualizado"),
            @ApiResponse(responseCode = "404", description = "El lugar no existe", content = @Content)
    })
    @PutMapping("/{id}")
    public VenueResponse updateVenue(@PathVariable Long id, @RequestBody @Valid CreateVenueRequest request) {
        Venue venueDomain = new Venue(null, request.getName(), request.getCity());
        return toResponse(venueUseCase.update(id, venueDomain));
    }

    @Operation(summary = "Eliminar lugar", description = "Borra permanentemente un lugar del sistema.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Lugar eliminado"),
            @ApiResponse(responseCode = "404", description = "El lugar no existe", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteVenue(@PathVariable Long id) {
        venueUseCase.delete(id);
    }

    private VenueResponse toResponse(Venue venue) {
        return new VenueResponse(venue.getId(), venue.getName(), venue.getCity());
    }
}