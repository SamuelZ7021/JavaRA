package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.DiagnosisEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiagnosisJpaRepository extends JpaRepository<DiagnosisEntity, Long> {

    // Método derivado para buscar el diagnóstico asociado a un ID de cita específico.
    // Útil para validaciones (ej: no permitir dos diagnósticos para la misma cita)
    Optional<DiagnosisEntity> findByAppointmentId(Long appointmentId);
}