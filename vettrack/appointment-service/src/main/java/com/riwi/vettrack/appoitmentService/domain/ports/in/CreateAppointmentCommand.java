package com.riwi.vettrack.appoitmentService.domain.ports.in;

import java.time.LocalDateTime;

public record CreateAppointmentCommand(
        Long petId,
        Long veterinarianId,
        LocalDateTime dateTime,
        String reason
) {}