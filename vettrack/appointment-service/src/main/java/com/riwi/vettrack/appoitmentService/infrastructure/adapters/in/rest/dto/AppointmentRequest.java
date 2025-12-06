package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor; // Importante para Jackson

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    @NotNull(message = "El ID de la mascota es obligatorio")
    @Positive(message = "El ID debe ser mayor a 0")
    private Long petId;

    @NotNull(message = "El ID del veterinario es obligatorio")
    private Long veterinarianId;

    @NotNull(message = "La fecha es obligatoria")
    @Future(message = "La cita debe programarse en el futuro") // Regla de negocio básica [cite: 347]
    private LocalDateTime dateTime;

    @NotNull(message = "El motivo es obligatorio")
    private String reason;
}