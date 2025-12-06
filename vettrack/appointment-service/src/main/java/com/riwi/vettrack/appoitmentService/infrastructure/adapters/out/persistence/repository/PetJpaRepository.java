package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.PetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PetJpaRepository extends JpaRepository<PetEntity, Long> {
    List<PetEntity> findByOwnerId(Long ownerId);
}