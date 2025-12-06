package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.AppointmentEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppointmentJpaRepository extends JpaRepository<AppointmentEntity, Long> {

    // 1. Para ADMIN: Trae todo
    @EntityGraph(attributePaths = {"veterinarian"})
    List<AppointmentEntity> findAll();

    // 2. Para DUEÑO: Trae solo las citas donde él es el owner
    @EntityGraph(attributePaths = {"veterinarian"})
    List<AppointmentEntity> findByOwnerId(Long ownerId);

    // 3. Para VETERINARIO: Trae solo las citas asignadas a su email (asumiendo username = email)
    @EntityGraph(attributePaths = {"veterinarian"})
    @Query("SELECT a FROM AppointmentEntity a JOIN a.veterinarian v WHERE v.email = :email")
    List<AppointmentEntity> findByVeterinarianEmail(@Param("email") String email);
}