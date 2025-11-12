package com.evento.envent.controller.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateVenueRequest {


    @NotBlank(message = "El nombre del lugar no puede estar vacío")
    @Size(min = 3, message = "El nombre del lugar debe tener al menos 3 caracteres")
    private String name;

    @NotBlank(message = "La ciudad no puede estar vacía")
    private String city;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }
}