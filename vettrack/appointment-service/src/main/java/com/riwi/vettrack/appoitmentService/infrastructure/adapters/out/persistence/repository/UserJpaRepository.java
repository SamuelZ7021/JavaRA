package com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.repository;

import com.riwi.vettrack.appoitmentService.infrastructure.adapters.out.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}