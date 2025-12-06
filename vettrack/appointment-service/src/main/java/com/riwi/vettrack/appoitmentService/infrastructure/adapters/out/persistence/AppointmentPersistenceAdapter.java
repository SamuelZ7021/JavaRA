package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence;

import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;
import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.domain.ports.out.AppointmentRepositoryPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.AppointmentEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.DiagnosisEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper.AppointmentMapper;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper.DiagnosisMapper;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.mapper.PetMapper;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.AppointmentJpaRepository;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.DiagnosisJpaRepository;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.PetJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AppointmentPersistenceAdapter implements AppointmentRepositoryPort {

    private final AppointmentJpaRepository appointmentJpaRepository;
    private final DiagnosisJpaRepository diagnosisJpaRepository;
    private final PetJpaRepository petJpaRepository; // Inyectar Repositorio Mascotas

    private final AppointmentMapper appointmentMapper;
    private final DiagnosisMapper diagnosisMapper;
    private final PetMapper petMapper;

    @Override
    public Appointment save(Appointment appointment) {
        var entity = appointmentMapper.toEntity(appointment);
        var saved = appointmentJpaRepository.save(entity);
        return appointmentMapper.toDomain(saved);
    }

    @Override
    public List<Appointment> findAll() {
        return appointmentMapper.toDomainList(appointmentJpaRepository.findAll());
    }

    @Override
    public List<Appointment> findByOwnerId(Long ownerId) {
        return appointmentMapper.toDomainList(appointmentJpaRepository.findByOwnerId(ownerId));
    }

    @Override
    public List<Appointment> findByVeterinarianEmail(String email) {
        return appointmentMapper.toDomainList(appointmentJpaRepository.findByVeterinarianEmail(email));
    }

    @Override
    public Appointment findById(Long id) {
        return appointmentJpaRepository.findById(id)
                .map(appointmentMapper::toDomain)
                .orElse(null);
    }

    @Override
    public void saveDiagnosis(Diagnosis diagnosis) {
        DiagnosisEntity entity = diagnosisMapper.toEntity(diagnosis);

        AppointmentEntity appointmentEntity = appointmentJpaRepository.findById(diagnosis.getAppointmentId())
                .orElseThrow(() -> new DomainException("Appointment not found for diagnosis ID: " + diagnosis.getAppointmentId()));

        entity.setAppointment(appointmentEntity);

        diagnosisJpaRepository.save(entity);
    }

    @Override
    public Pet findPetById(Long id) {
        return petJpaRepository.findById(id)
                .map(petMapper::toDomain)
                .orElse(null);
    }
}