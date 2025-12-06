package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity;

import com.riwi.vettrack.appoitmentService.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username; // Será el email

    @Column(nullable = false)
    private String password; // Encriptada

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
}