package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence;

import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.domain.ports.out.PetRepositoryPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.PetEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper.PetMapper;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.PetJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PetPersistenceAdapter implements PetRepositoryPort {

    private final PetJpaRepository petJpaRepository;
    private final PetMapper petMapper;

    @Override
    public Pet save(Pet pet) {
        PetEntity entity = petMapper.toEntity(pet);
        PetEntity saved = petJpaRepository.save(entity);
        return petMapper.toDomain(saved);
    }

    @Override
    public Optional<Pet> findById(Long id) {
        return petJpaRepository.findById(id)
                .map(petMapper::toDomain);
    }

    @Override
    public List<Pet> findAll() {
        return petMapper.toDomainList(petJpaRepository.findAll());
    }

    @Override
    public List<Pet> findByOwnerId(Long ownerId) {
        return petMapper.toDomainList(petJpaRepository.findByOwnerId(ownerId));
    }
}