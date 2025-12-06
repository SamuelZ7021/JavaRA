package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity;

import jakarta.persistence.*;
import lombok.*; // Importamos todo Lombok

@Entity
@Table(name = "veterinarians")
@Getter
@Setter
@AllArgsConstructor // <--- CRÍTICO: Crea el constructor con todos los campos
@NoArgsConstructor  // <--- CRÍTICO: Necesario para JPA/Hibernate
@Builder            // <--- CRÍTICO: Nos ayuda a crear objetos en los tests
public class VeterinarianEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @Column(nullable = false)
    private boolean active;
}