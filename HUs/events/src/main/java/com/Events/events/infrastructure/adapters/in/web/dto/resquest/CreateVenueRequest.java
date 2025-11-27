package com.Events.events.infrastructure.adapters.in.web.dto.resquest;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "DTO para crear o actualizar un Lugar (Venue)")
public class CreateVenueRequest {

    @Schema(description = "Nombre del lugar", example = "Movistar Arena", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "El nombre del lugar no puede estar vacío")
    @Size(min = 3, message = "El nombre del lugar debe tener al menos 3 caracteres")
    private String name;

    @Schema(description = "Ciudad donde se ubica", example = "Bogotá", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    public CreateVenueRequest() {
    }

    // Getters y Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getAddress() {
        return address;
    }
    public void setAddress(String address) {
        this.address = address;
    }
}