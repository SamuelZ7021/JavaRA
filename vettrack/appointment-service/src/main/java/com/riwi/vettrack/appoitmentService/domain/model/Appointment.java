package com.riwi.vettrack.appoitmentService.domain.model;

import com.riwi.vettrack.appoitmentService.domain.enums.AppointmentStatus;
import java.time.LocalDateTime;

public class Appointment {
    private Long id;
    private Long petId; // Corregido: Era PetStatus, debe ser ID para relacionar
    private Veterinarian veterinarian;
    private LocalDateTime dateTime;
    private String reason;
    private AppointmentStatus status;
    private String cancellationReason;
    private Long ownerId;

    // 1. Constructor Vacío (OBLIGATORIO para MapStruct/JPA/Librerías)
    public Appointment() {
    }

    // 2. Constructor Completo
    public Appointment(Long id, Long petId, Veterinarian veterinarian,
                       LocalDateTime dateTime, String reason,
                       AppointmentStatus status, String cancellationReason) {
        this.id = id;
        this.petId = petId;
        this.veterinarian = veterinarian;
        this.dateTime = dateTime;
        this.reason = reason;
        this.status = status;
        this.cancellationReason = cancellationReason;
    }

    // Lógica de Dominio
    public void confirm() {
        if (this.status == AppointmentStatus.PENDIENTE) {
            this.status = AppointmentStatus.CONFIRMADA;
        }
    }

    public void cancel(String reason) {
        if (this.status != AppointmentStatus.REALIZADA) {
            this.status = AppointmentStatus.CANCELADA;
            this.cancellationReason = reason;
        }
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getPetId() { return petId; }
    public void setPetId(Long petId) { this.petId = petId; }

    public Veterinarian getVeterinarian() { return veterinarian; }
    public void setVeterinarian(Veterinarian veterinarian) { this.veterinarian = veterinarian; }

    public LocalDateTime getDateTime() { return dateTime; }
    public void setDateTime(LocalDateTime dateTime) { this.dateTime = dateTime; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public AppointmentStatus getStatus() { return status; }
    public void setStatus(AppointmentStatus status) { this.status = status; }

    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }

    public Long getOwnerId() {
        return ownerId;
    }
    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }
}