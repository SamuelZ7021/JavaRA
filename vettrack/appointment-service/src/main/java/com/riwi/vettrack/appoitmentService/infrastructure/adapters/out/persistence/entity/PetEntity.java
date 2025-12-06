package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity;

import com.riwi.vettrack.appoitmentService.domain.enums.PetStatus;
import com.riwi.vettrack.appoitmentService.domain.enums.Species;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "pets")
@Getter @Setter
public class PetEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Species species;

    private String race;

    @Column(nullable = false)
    private Integer age;

    @Column(name = "owner_name", nullable = false)
    private String ownerName;

    @Column(name = "owner_document", nullable = false)
    private String ownerDocument;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PetStatus status;
}