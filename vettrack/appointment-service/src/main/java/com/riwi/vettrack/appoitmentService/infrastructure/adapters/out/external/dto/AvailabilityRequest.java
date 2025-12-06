package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.external.dto;

import java.time.LocalDate;
import java.time.LocalTime;

// Usamos Records para DTOs inmutables (Best Practice Java 17)
public record AvailabilityRequest(
        Long veterinarioId,
        LocalDate fecha,
        LocalTime hora
) {}