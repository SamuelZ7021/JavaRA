package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence;

import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VeterinarianRepositoryPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.VeterinarianEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper.VeterinarianMapper;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.VeterinarianJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class VeterinarianPersistenceAdapter implements VeterinarianRepositoryPort {

    private final VeterinarianJpaRepository jpaRepository;
    private final VeterinarianMapper mapper;

    @Override
    public Optional<Veterinarian> findById(Long id) {
        return jpaRepository.findById(id)
                .map(mapper::toDomain);
    }

    @Override
    public Veterinarian save(Veterinarian veterinarian) {
        // 1. Convertir Dominio -> Entidad
        VeterinarianEntity entity = mapper.toEntity(veterinarian);

        // 2. Guardar y RECIBIR la entidad con el ID nuevo
        VeterinarianEntity savedEntity = jpaRepository.save(entity);

        // 3. Convertir Entidad Guardada -> Dominio (¡Ahora lleva el ID!)
        return mapper.toDomain(savedEntity);
    }
}