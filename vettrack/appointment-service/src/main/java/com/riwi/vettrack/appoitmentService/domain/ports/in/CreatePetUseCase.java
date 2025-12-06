package com.riwi.vettrack.appoitmentService.domain.ports.in;

import com.riwi.vettrack.appoitmentService.domain.model.Pet;

import java.util.List;

public interface CreatePetUseCase {
    Pet createPet(CreatePetCommand command);
    List<Pet> listPets();
}