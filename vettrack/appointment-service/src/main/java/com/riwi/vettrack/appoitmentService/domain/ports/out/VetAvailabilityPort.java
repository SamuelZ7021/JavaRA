package com.riwi.vettrack.appoitmentService.domain.ports.out;

import java.time.LocalDateTime;

public interface VetAvailabilityPort {
    // Retorna true si está disponible, false si no.
    // Podríamos retornar un objeto más complejo con el motivo, pero empecemos simple.
    AvailabilityResponse checkAvailability(Long veterinarianId, LocalDateTime dateTime);

    // Record (Java 17+) para transportar la respuesta en el dominio
    record AvailabilityResponse(boolean isAvailable, String reason) {}
}