package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.VeterinarianEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VeterinarianJpaRepository extends JpaRepository<VeterinarianEntity, Long> {

}
