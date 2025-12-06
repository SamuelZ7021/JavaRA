package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity;

import com.riwi.vettrack.appoitmentService.domain.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
@Getter @Setter
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private VeterinarianEntity veterinarian;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Column(name = "pet_id", nullable = false)
    private Long petId;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AppointmentStatus status;

    private String cancellationReason;
}
