package com.riwi.vetmock.controller;

import org.springframework.web.bind.annotation.*;
import java.util.Objects;
import java.util.Random;

@RestController
@RequestMapping("/availability")
public class MockAvailabilityController {

    @PostMapping
    public AvailabilityDTO.Response checkAvailability(@RequestBody AvailabilityDTO.Request request) {
        // Validación básica
        if (request.getVeterinarioId() == null || request.getFecha() == null || request.getHora() == null) {
            throw new IllegalArgumentException("Todos los campos son obligatorios");
        }

        // 1. Generar un hash combinando los tres valores (Determinismo) [cite: 89]
        // Usamos Objects.hash para garantizar que mismos inputs den mismo output
        int hash = Objects.hash(request.getVeterinarioId(), request.getFecha(), request.getHora());

        // 2. Aplicar lógica de disponibilidad [cite: 90-91]
        // Math.abs para evitar negativos en el módulo
        boolean isAvailable = (Math.abs(hash) % 2) == 0;

        // 3. Construir respuesta
        String motivo = isAvailable ? "Horario libre" : "Agenda ocupada";

        // Simular un pequeño delay de red (Opcional, para realismo)
        try { Thread.sleep(100); } catch (InterruptedException e) {}

        return new AvailabilityDTO.Response(
                request.getVeterinarioId(),
                isAvailable,
                motivo
        );
    }
}