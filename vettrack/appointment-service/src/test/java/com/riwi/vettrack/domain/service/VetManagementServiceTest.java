package com.riwi.vettrack.domain.service;

import com.riwi.vettrack.appoitmentService.domain.enums.AppointmentStatus;
import com.riwi.vettrack.appoitmentService.domain.exception.DomainException;
import com.riwi.vettrack.appoitmentService.domain.model.Appointment;
import com.riwi.vettrack.appoitmentService.domain.model.Veterinarian;
import com.riwi.vettrack.appoitmentService.domain.ports.out.AppointmentRepositoryPort;
import com.riwi.vettrack.appoitmentService.domain.ports.out.VeterinarianRepositoryPort;
import com.riwi.vettrack.appoitmentService.domain.service.VetManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VetManagementServiceTest {

    @Mock
    private VeterinarianRepositoryPort veterinarianRepository;

    @Mock
    private AppointmentRepositoryPort appointmentRepository;

    private VetManagementService vetManagementService;

    @BeforeEach
    void setUp() {
        vetManagementService = new VetManagementService(veterinarianRepository, appointmentRepository);
    }

    @Test
    @DisplayName("Should deactivate vet and cancel all future appointments")
    void shouldDeactivateVetAndCancelAppointments() {
        // GIVEN
        Long vetId = 1L;
        // Creamos el veterinario completo
        Veterinarian vet = new Veterinarian(vetId, "Dr. House", "house@tv.com", "123", true);

        // CORRECCIÓN: Usamos el nuevo constructor completo de Appointment
        // (id, petId, veterinarian, dateTime, reason, status, cancellationReason)
        Appointment app1 = new Appointment(
                101L,
                500L,
                vet,
                LocalDateTime.now().plusDays(1),
                "Checkup",
                AppointmentStatus.PENDIENTE,
                null
        );

        Appointment app2 = new Appointment(
                102L,
                501L,
                vet,
                LocalDateTime.now().plusDays(2),
                "Vaccine",
                AppointmentStatus.CONFIRMADA,
                null
        );

        List<Appointment> futureAppointments = Arrays.asList(app1, app2);

        when(veterinarianRepository.findById(vetId)).thenReturn(Optional.of(vet));
        when(appointmentRepository.findByOwnerId(vetId)).thenReturn(futureAppointments);

        // WHEN
        vetManagementService.desactiveVeterinarian(vetId);

        // THEN
        ArgumentCaptor<Veterinarian> vetCaptor = ArgumentCaptor.forClass(Veterinarian.class);
        verify(veterinarianRepository).save(vetCaptor.capture());
        assertFalse(vetCaptor.getValue().isActive());

        verify(appointmentRepository, times(2)).save(any(Appointment.class));

        assertEquals(AppointmentStatus.CANCELADA, app1.getStatus());
        // Verificamos el mensaje exacto que definiste en tu servicio
        assertEquals("System Auto-Cancellation: Veterinarian 1 is no longer active.", app1.getCancellationReason());

        assertEquals(AppointmentStatus.CANCELADA, app2.getStatus());
    }

    @Test
    @DisplayName("Should throw DomainException when veterinarian does not exist")
    void shouldThrowExceptionWhenVetNotFound() {
        Long vetId = 99L;
        when(veterinarianRepository.findById(vetId)).thenReturn(Optional.empty());

        DomainException exception = assertThrows(DomainException.class, () -> {
            vetManagementService.desactiveVeterinarian(vetId);
        });

        assertEquals("Veterinarian not found, with ID: 99", exception.getMessage());

        verify(appointmentRepository, never()).findByOwnerId(any());
        verify(veterinarianRepository, never()).save(any());
    }
}