package com.riwi.academico.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "student")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_student")
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    /**
     * Set of enrollments associated with this student.
     * mappedBy = "student": Indicates that the 'student' field in the Enrollment entity owns the relationship.
     * cascade = CascadeType.ALL: Operations (PERSIST, MERGE, REMOVE...) on Student wiññ cascade to it's the Enrollments.
     * orphanRemoval = true: If an Enrollments is removed from this set, it should be deleted from the database.
     * fetch = FetchType.EAGER: Load Enrollments along with the student. Be cautious with this in complex scenarios.
     */
    @OneToMany(
            mappedBy = "student",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @Builder.Default
    private Set<Enrollment> enrollments = new HashSet<>();
}
