package com.riwi.vettrack.appoitmentService.domain.ports.out;

import com.riwi.vettrack.appoitmentService.domain.model.Pet;

import java.util.List;
import java.util.Optional;

public interface PetRepositoryPort {
    Pet save(Pet pet);
    Optional<Pet> findById(Long id);
    List<Pet> findAll();
    List<Pet> findByOwnerId(Long ownerId);
}