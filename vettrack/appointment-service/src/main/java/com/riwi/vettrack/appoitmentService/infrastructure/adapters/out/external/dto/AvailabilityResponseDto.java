package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.external.dto;

public record AvailabilityResponseDto(
        Long veterinarioId,
        boolean disponible,
        String motivo
) {}