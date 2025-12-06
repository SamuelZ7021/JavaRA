package com.riwi.vettrack.appoitmentService.domain.ports.in;

import com.riwi.vettrack.appoitmentService.domain.enums.Species;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CreatePetCommand {
    private String name;
    private String ownerName;
    private String ownerDocument;
    private Species species;
    private String race;
    private Integer age;
}