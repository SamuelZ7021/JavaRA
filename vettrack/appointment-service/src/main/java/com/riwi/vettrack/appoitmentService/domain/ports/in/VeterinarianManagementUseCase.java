package com.riwi.vettrack.appoitmentService.domain.ports.in;

import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;

public interface VeterinarianManagementUseCase {
    Veterinarian create(Veterinarian veterinarian);
    Veterinarian getById(Long id);
    Veterinarian update(Long id, Veterinarian veterinarian); // Nuevo método
    void delete(Long id); // Borrado lógico (Inactivar)
}