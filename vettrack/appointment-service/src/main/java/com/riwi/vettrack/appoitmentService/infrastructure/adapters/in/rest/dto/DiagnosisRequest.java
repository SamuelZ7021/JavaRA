package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DiagnosisRequest {
    @NotNull(message = "El ID de la cita es obligatorio")
    private Long appointmentId;

    @NotBlank(message = "La descripción es obligatoria")
    private String description;

    @NotBlank(message = "El tratamiento es obligatorio")
    private String treatment;

    @NotBlank(message = "Las recomendaciones son obligatorias")
    private String recommendations;
}