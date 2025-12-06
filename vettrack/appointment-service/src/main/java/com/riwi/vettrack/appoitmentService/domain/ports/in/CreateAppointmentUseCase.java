package com.riwi.vettrack.appoitmentService.domain.ports.in;

import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;

import java.util.List;

public interface CreateAppointmentUseCase {
    Appointment createAppointment(CreateAppointmentCommand command);
    List<Appointment> listAppointments();
    Diagnosis createDiagnosis(Diagnosis diagnosis);
}