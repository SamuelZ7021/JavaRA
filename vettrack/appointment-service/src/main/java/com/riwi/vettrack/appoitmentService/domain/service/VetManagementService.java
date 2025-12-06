package com.riwi.vettrack.appoitmentService.domain.service;

import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.out.AppointmentRepositoryPort;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VeterinarianRepositoryPort;

import java.util.List;

public class VetManagementService {

    private final VeterinarianRepositoryPort vetRepositoryPort;
    private final AppointmentRepositoryPort appoRepositoryPort;

    public VetManagementService(VeterinarianRepositoryPort vetRepositoryPort,
                                AppointmentRepositoryPort appoRepositoryPort) {
        this.vetRepositoryPort = vetRepositoryPort;
        this.appoRepositoryPort = appoRepositoryPort;
    }

    public void desactiveVeterinarian(Long vetId) {
        Veterinarian vet = vetRepositoryPort.findById(vetId)
                .orElseThrow(() -> new DomainException("Veterinarian not found, with ID: " + vetId));

        vet.deactivete();

        vetRepositoryPort.save(vet);

        List<Appointment> affectedAppointments = appoRepositoryPort.findFutureAppointmentsByVetId(vetId);

        for (Appointment appointment : affectedAppointments){
            appointment.cancel("System Auto-Cancellation: Veterinarian " + vetId + " is no longer active.");
            appoRepositoryPort.save(appointment);
        }
    }
}
