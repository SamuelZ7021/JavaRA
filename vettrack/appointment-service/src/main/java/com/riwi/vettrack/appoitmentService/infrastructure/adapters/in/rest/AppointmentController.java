package com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest;

import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreateAppointmentCommand;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreateAppointmentUseCase;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.AppointmentRequest;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.in.rest.dto.DiagnosisRequest;
import jakarta.validation.Valid;
import jdk.dynalink.linker.LinkerServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final CreateAppointmentUseCase createAppointmentUseCase;

    @PostMapping
    public ResponseEntity<Appointment> create(@RequestBody @Valid AppointmentRequest request) {
        // CORRECCIÓN: Usamos getters tradicionales (get...) en lugar de acceso directo
        var command = new CreateAppointmentCommand(
                request.getPetId(),          // Antes: request.petId()
                request.getVeterinarianId(), // Antes: request.veterinarianId()
                request.getDateTime(),       // Antes: request.dateTime()
                request.getReason()          // Antes: request.reason()
        );

        Appointment created = createAppointmentUseCase.createAppointment(command);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> listAll(){
        List<Appointment> appointments = createAppointmentUseCase.listAppointments();

        if(appointments.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(appointments);
    }

    @PostMapping("/diagnosis")
    public ResponseEntity<Diagnosis> createDiagnosis(@RequestBody @Valid DiagnosisRequest request) {
        // Mapeo simple DTO -> Dominio
        Diagnosis diagnosis = new Diagnosis(
                null,
                request.getDescription(),
                request.getTreatment(),
                request.getRecommendations(),
                request.getAppointmentId()
        );

        Diagnosis created = createAppointmentUseCase.createDiagnosis(diagnosis);

        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}