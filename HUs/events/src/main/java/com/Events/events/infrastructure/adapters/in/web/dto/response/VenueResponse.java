package com.Events.events.infrastructure.adapters.in.web.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Respuesta con la información pública de un Lugar")
public record VenueResponse(
        @Schema(description = "ID único del lugar", example = "1")
        Long id,

        @Schema(description = "Nombre comercial", example = "Movistar Arena")
        String name,

        @Schema(description = "Ciudad de ubicación", example = "Bogotá")
        String city
) {}