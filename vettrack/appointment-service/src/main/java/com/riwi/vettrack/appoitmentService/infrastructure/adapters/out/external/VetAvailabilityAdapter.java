package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.external;

import com.riwi.vettrack.appoitmentService.domain.ports.out.VetAvailabilityPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.external.dto.AvailabilityRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.external.dto.AvailabilityResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;

@Component
public class VetAvailabilityAdapter implements VetAvailabilityPort {

    private final RestClient restClient;

    public VetAvailabilityAdapter(@Value("${app.client.vet-availability-url}") String url) {
        // Inicializamos el cliente con la URL base configurada
        this.restClient = RestClient.builder()
                .baseUrl(url)
                .build();
    }

    @Override
    public AvailabilityResponse checkAvailability(Long veterinarianId, LocalDateTime dateTime) {
        // 1. Mapear datos de Dominio -> DTO Externo
        var request = new AvailabilityRequest(
                veterinarianId,
                dateTime.toLocalDate(),
                dateTime.toLocalTime()
        );

        // 2. Hacer la llamada POST
        // El mock espera: POST /availability
        AvailabilityResponseDto response = restClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .body(request)
                .retrieve()
                .body(AvailabilityResponseDto.class);

        // 3. Mapear DTO Externo -> Dominio (Protegemos al dominio de cambios externos)
        if (response == null) {
            // Fallback por seguridad
            return new AvailabilityResponse(false, "Error de comunicación con servicio externo");
        }

        return new AvailabilityResponse(response.disponible(), response.motivo());
    }
}