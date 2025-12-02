package com.evento.envent.controller;

import com.evento.envent.controller.dto.CreateVenueRequest;
import com.evento.envent.controller.dto.VenueResponse;
import com.evento.envent.services.VenueService;
// --- Imports de Swagger ---
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

// --- Imports de Spring y Validations
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/venues") // Ruta base para todos los endpoints de esta clase
@Tag(name = "Gestión de Lugares (Venues)", description = "API para el CRUD de Lugares (Versión en Memoria H1)")
public class VenueController {

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @Operation(summary = "Obtener todos los lugares", description = "Retorna una lista de todos los lugares en memoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de lugares obtenida exitosamente")
    })
    @GetMapping
    public List<VenueResponse> getAllVenues() {
        return venueService.findAll();
    }

    @Operation(summary = "Obtener un lugar por ID", description = "Retorna un lugar específico basado en su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar encontrado"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public VenueResponse getVenueById(@PathVariable Long id) {
        return venueService.findById(id);
    }

    @Operation(summary = "Crear un nuevo lugar", description = "Crea un nuevo lugar y lo almacena en memoria.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Lugar creado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos (ej. campos vacíos)", content = @Content)
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED) // Retorna HTTP 201
    public VenueResponse createVenue(@RequestBody @Valid CreateVenueRequest request) {
        return venueService.createVenue(request);
    }

    @Operation(summary = "Actualizar un lugar existente", description = "Actualiza un lugar por su ID con la información proporcionada.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lugar actualizado exitosamente",
                    content = @Content(schema = @Schema(implementation = VenueResponse.class))),
            @ApiResponse(responseCode = "400", description = "Datos de solicitud inválidos", content = @Content),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public VenueResponse updateVenue(@PathVariable Long id,
                                     @RequestBody @Valid CreateVenueRequest request) {
        return venueService.updateVenue(id, request);
    }

    @Operation(summary = "Eliminar un lugar", description = "Elimina un lugar existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Lugar eliminado exitosamente"),
            @ApiResponse(responseCode = "404", description = "Lugar no encontrado", content = @Content)
    })
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT) // Retorna HTTP 204
    public void deleteVenue(@PathVariable Long id) {
        venueService.deleteVenue(id);
    }
}