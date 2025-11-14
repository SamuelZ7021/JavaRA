package com.evento.envent.controller.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class CreateEventRequest {

    @NotBlank(message = "El nombre no puede estar vacio")
    @Size(min = 3, message = "El nombre dene tener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "La categoria no puedo estar vacía")
    private String category;

    @NotBlank(message = "la fecha debe de ser Ej: 2026-09-19T11:00:00")
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "El fecha de inicio debe ser en el dias antes del evento")
    private LocalDateTime startDate;

    @NotBlank(message = "El ID del lugar (venue) des obligatorio")
    private Long venueId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }
}
