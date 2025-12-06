package com.riwi.vettrack.appoitmentService.application.service;

import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.in.VeterinarianManagementUseCase;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VeterinarianRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VeterinarianManagementService implements VeterinarianManagementUseCase {

    private final VeterinarianRepositoryPort repositoryPort;

    @Override
    @Transactional
    public Veterinarian create(Veterinarian veterinarian) {
        return repositoryPort.save(veterinarian);
    }

    @Override
    public Veterinarian getById(Long id) {
        return repositoryPort.findById(id)
                .orElseThrow(() -> new DomainException("Veterinarian not found"));
    }

    @Override
    @Transactional
    public Veterinarian update(Long id, Veterinarian veterinarian) {
        // 1. Buscar el existente
        Veterinarian current = getById(id);

        // 2. Actualizar campos (Aquí podrías usar un mapper también)
        // Nota: Usamos un constructor nuevo o setters si los tienes en el dominio
        Veterinarian updated = new Veterinarian(
                current.getId(), // Mantenemos el ID original
                veterinarian.getName(),
                veterinarian.getEmail(),
                veterinarian.getPhone(),
                veterinarian.getActive() // Aquí tomamos el estado que venga del request
        );

        // 3. Guardar
        return repositoryPort.save(updated);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        // Soft Delete: Solo cambiamos el estado a inactivo
        Veterinarian current = getById(id);
        // Como tu modelo es inmutable en los ejemplos anteriores, creamos copia
        Veterinarian disabled = new Veterinarian(
                current.getId(),
                current.getName(),
                current.getEmail(),
                current.getPhone(),
                false // <--- Desactivar explícitamente
        );
        repositoryPort.save(disabled);
    }
}