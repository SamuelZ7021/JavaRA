package com.riwi.vettrack.appoitmentService.application.service;

import com.riwi.vettrack.appoitmentService.domain.enums.AppointmentStatus;
import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Diagnosis;
import com.riwi.vettrack.appoitmentService.domain.model.Pet;
import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreateAppointmentCommand;
import com.riwi.vettrack.appoitmentService.domain.ports.in.CreateAppointmentUseCase;
import com.riwi.vettrack.appoitmentService.domain.ports.out.AppointmentRepositoryPort;
import com.riwi.vettrack.appoitmentService.domain.ports.out.PetRepositoryPort;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VetAvailabilityPort;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VeterinarianRepositoryPort;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.UserEntity;
import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentManagementService implements CreateAppointmentUseCase {

    private final AppointmentRepositoryPort appointmentRepositoryPort;
    private final VeterinarianRepositoryPort veterinarianRepositoryPort;
    private final PetRepositoryPort petRepositoryPort;
    private final VetAvailabilityPort vetAvailabilityPort;
    private final UserJpaRepository userJpaRepository;


    @Override
    @Transactional
    public Appointment createAppointment(CreateAppointmentCommand command) {

        String username = ((UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
        UserEntity currentUser = userJpaRepository.findByUsername(username)
                .orElseThrow(() -> new DomainException("User not found"));

        // 1. RECUPERAR DATOS REALES (No confíes en el ID solo)
        Veterinarian vet = veterinarianRepositoryPort.findById(command.veterinarianId())
                .orElseThrow(() -> new DomainException("Veterinarian not found with ID: " + command.veterinarianId()));

        // 2. VALIDACIÓN DE NEGOCIO: ¿Está activo?
        if (!vet.isActive()) { // Asume que Veterinarian tiene un método isActive() o getActive() que retorna boolean
            throw new DomainException("Cannot schedule appointment: Veterinarian is inactive.");
        }

        Pet pet = petRepositoryPort.findPetById(command.petId()); // Necesitas este método

        if (pet == null) {
            throw new DomainException("Pet not found with ID:" + command.petId());
        }

        if (!pet.isActive()) { // Usamos el método de dominio que creamos
            throw new DomainException("The pet is not ACTIVE and cannot schedule appointments.");
        }

        // 3. CONSTRUCCIÓN PURA
        Appointment appointment = new Appointment();
        appointment.setPetId(command.petId());
        appointment.setVeterinarian(vet);
        appointment.setDateTime(command.dateTime());
        appointment.setReason(command.reason());
        appointment.setStatus(AppointmentStatus.PENDIENTE);
        appointment.setOwnerId(currentUser.getId());

        // 4. CONSULTA EXTERNA
        var availability = vetAvailabilityPort.checkAvailability(vet.getId(), appointment.getDateTime());

        if (availability.isAvailable()) {
            appointment.confirm();
        } else {
            appointment.cancel("Auto-Rejection: " + availability.reason());
        }

        // 5. GUARDAR Y RETORNAR (Fix del ID null)
        return appointmentRepositoryPort.save(appointment);
    }

    @Transactional(readOnly = true)
    public List<Appointment> listAppointments() {
        // 1. Obtener usuario y rol del contexto de seguridad
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream()
                .findFirst().orElseThrow().getAuthority();

        // 2. Switch expression (Java 17+) para elegir la estrategia
        return switch (role) {
            case "ADMIN" -> appointmentRepositoryPort.findAll();

            case "VETERINARIO" -> appointmentRepositoryPort.findByVeterinarianEmail(username);

            case "DUENO" -> {
                UserEntity user = userJpaRepository.findByUsername(username)
                        .orElseThrow(() -> new DomainException("User not found"));
                yield appointmentRepositoryPort.findByOwnerId(user.getId());
            }

            default -> List.of();
        };
    }
    @Transactional
    @Override
    public Diagnosis createDiagnosis(Diagnosis diagnosis) {
        // 1. Obtener la Cita
        Appointment appointment = appointmentRepositoryPort.findById(diagnosis.getAppointmentId());
        if (appointment == null) {
            throw new DomainException("Appointment not found for diagnosis ID: " + diagnosis.getAppointmentId());
        }

        // 2. VALIDACIÓN DE SEGURIDAD: ¿Es el veterinario asignado?
        String currentUserEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        String currentRole = SecurityContextHolder.getContext().getAuthentication().getAuthorities()
                .stream().findFirst().get().getAuthority();

        if ("VETERINARIO".equals(currentRole)) {
            // Asumimos que el email del veterinario está en su entidad.
            // Si tu objeto de dominio 'Veterinarian' no tiene email, debes cargarlo o ajustar esta validación.
            if (!appointment.getVeterinarian().getEmail().equals(currentUserEmail)) {
                throw new DomainException("Access Denied: Only the assigned veterinarian can record the diagnosis.");
            }
        }

        // 3. Validar Estado de la Cita
        if (appointment.getStatus() != AppointmentStatus.CONFIRMADA) {
            throw new DomainException("Only confirmed appointments can be diagnosed.");
        }

        // 4. Guardar Diagnóstico
        appointmentRepositoryPort.saveDiagnosis(diagnosis);

        // 5. Actualizar estado de la cita a REALIZADA (opcional, pero recomendado)
        appointment.setStatus(AppointmentStatus.REALIZADA); // Asegúrate de tener este ENUM
        appointmentRepositoryPort.save(appointment);

        return diagnosis;
    }
}
