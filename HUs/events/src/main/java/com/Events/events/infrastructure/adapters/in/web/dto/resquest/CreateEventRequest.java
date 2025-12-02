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

    @NotNull(message = "La fecha de inicio es obligatoria")
    @Future(message = "La fecha de inicio debe ser en el futuro")
    // Usamos un patrón que acepta la 'Z' literal al final si viene de Swagger
    // OJO: Esto asume que la fecha viene en UTC si trae Z.
    // Jackson es inteligente: si le damos un patrón compatible, lo parsea.
    // Al poner 'Z' entre comillas simples, le decimos "espera una letra Z literal".
    // Pero mejor aún: NO USAR PATRÓN y dejar que @JsonFormat use ISO por defecto,
    // pero forzamos a que Spring/Jackson acepte la Z convirtiéndola.
    // LA SOLUCIÓN REAL: Usar java.time.Instant o ZonedDateTime para la entrada.
    // Pero para no romper tu código JPA, usaremos LocalDateTime y le diremos a Jackson que ignore la zona.
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", shape = JsonFormat.Shape.STRING)
    private LocalDateTime startDate;

    @NotNull(message = "La fecha de fin es obligatoria")
    @Future(message = "La fecha de fin debe ser en el futuro")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSX", shape = JsonFormat.Shape.STRING)
    private LocalDateTime endDate;

    @NotNull(message = "El ID del venue es obligatorio")
    private Long venueId;
}