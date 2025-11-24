package com.Events.events.infrastructure.adapters.out.jpa.repository;

import com.Events.events.infrastructure.adapters.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataUserRepository extends JpaRepository<UserEntity, Long> {
}
