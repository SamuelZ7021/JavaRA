package com.riwi.vettrack.appoitmentService.domain.ports.out;

import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;
import com.riwi.vettrack.appoitmentService.domain.model.Pet;

import java.util.List;

public interface AppointmentRepositoryPort {
    Appointment save(Appointment appointment);

    // Métodos nuevos para filtrado por roles
    List<Appointment> findAll();
    List<Appointment> findByOwnerId(Long ownerId);
    List<Appointment> findByVeterinarianEmail(String email);

    // Método auxiliar útil
    Appointment findById(Long id);

    void saveDiagnosis(Diagnosis diagnosis);

    Pet findPetById(Long id);
}