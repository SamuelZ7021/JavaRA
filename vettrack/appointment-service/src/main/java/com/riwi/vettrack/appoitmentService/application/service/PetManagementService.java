package com.riwi.vettrack.appoitmentService.application.service;

import com.riwi.vettrack.appoitmentService.domain.enums.PetStatus;
import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreatePetCommand;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreatePetUseCase;
import com.riwi.vettrack.appoitmentService.domain.ports.out.PetRepositoryPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetManagementService implements CreatePetUseCase {

    private final PetRepositoryPort petRepositoryPort;
    private final UserJpaRepository userJpaRepository; // Para obtener ID del dueño

    @Override
    @Transactional
    public Pet createPet(CreatePetCommand command) {
        // 1. Obtener Usuario Autenticado (El dueño de la mascota)
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity currentUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User not found"));

        // 2. Construir la Mascota
        Pet pet = new Pet();
        pet.setName(command.getName());
        pet.setOwnerName(command.getOwnerName());
        pet.setOwnerDocument(command.getOwnerDocument());
        pet.setSpecies(command.getSpecies());
        pet.setRace(command.getRace());
        pet.setAge(command.getAge());

        // Reglas de Negocio Automáticas
        pet.setStatus(PetStatus.ACTIVA); // Siempre nace activa
        pet.setOwnerId(currentUser.getId()); // Vinculación de seguridad

        // 3. Guardar
        return petRepositoryPort.save(pet);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Pet> listPets() {
        // 1. Identificar Usuario y Rol
        var auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        String role = auth.getAuthorities().stream().findFirst().get().getAuthority();

        // 2. Filtrar según Rol
        if ("DUENO".equals(role)) {
            UserEntity user = userJpaRepository.findByUsername(username)
                    .orElseThrow(() -> new DomainException("User not found"));
            return petRepositoryPort.findByOwnerId(user.getId());
        } else {
            // ADMIN y VETERINARIO ven todo
            return petRepositoryPort.findAll();
        }
    }
}