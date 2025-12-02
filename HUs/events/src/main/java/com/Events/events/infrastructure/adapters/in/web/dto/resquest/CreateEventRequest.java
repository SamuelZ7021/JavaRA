package com.Events.events.infrastructure.adapters.in.web.dto.resquest;

import com.Events.events.infrastructure.adapters.in.web.validation.ValidDateRange;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidDateRange(message = "La fecha de inicio debe ser anterior a la fecha de fin")
public class CreateEventRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "La categoría es obligatoria")
    private String category;

    // En CreateEventRequest.java

    // Eliminamos la 'X' del final
    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING) // <-- CAMBIO AQUÍ
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS", shape = JsonFormat.Shape.STRING) // <-- CAMBIO AQUÍ
    private LocalDateTime endDate;

    @NotNull(message = "El ID del venue es obligatorio")
    private Long venueId;
}