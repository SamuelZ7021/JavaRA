package com.riwi.vettrack.appoitmentService.domain.ports.out;

import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;

import java.util.Optional;

public interface VeterinarianRepositoryPort {
    Optional<Veterinarian> findById(Long id);
    Veterinarian save (Veterinarian veterinarian);
}
