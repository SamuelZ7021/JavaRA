package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto;

import com.riwi.vettrack.appoitmentService.domain.enums.Species;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PetRequest {
    @NotBlank(message = "El nombre es obligatorio")
    private String name;

    @NotBlank(message = "El nombre del dueño es obligatorio")
    private String ownerName;

    @NotBlank(message = "El documento del dueño es obligatorio")
    private String ownerDocument;

    @NotNull(message = "La especie es obligatoria")
    private Species species; // PERRO, GATO, etc.

    @NotBlank(message = "La raza es obligatoria")
    private String race;

    @NotNull(message = "La edad es obligatoria")
    @Min(value = 0, message = "La edad no puede ser negativa")
    private Integer age;
}