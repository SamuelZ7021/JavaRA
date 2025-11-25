package com.Events.events.infrastructure.adapters.in.web.dto.resquest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Schema(description = "DTO para la creación o actualización de un evento")
public class CreateEventRequest {

    @Schema(description = "Nombre del evento (debe ser único)", example = "Concierto de Rock")
    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 3, message = "El nombre debe tener al menos 3 caracteres")
    private String name;

    @Schema(description = "Categoría del evento", example = "Música")
    @NotBlank(message = "La categoría no puede estar vacía")
    private String category;

    @Schema(description = "Fecha y hora de inicio (debe ser futura)", example = "2025-12-25T20:00:00")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser en el futuro")
    private LocalDateTime startDate;

    @Schema(description = "ID del lugar (Venue) donde ocurrirá el evento", example = "1")
    @NotNull(message = "El ID del lugar (venue) es obligatorio")
    private Long venueId;

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getStartDate() { return startDate; }
    public void setStartDate(LocalDateTime startDate) { this.startDate = startDate; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
}