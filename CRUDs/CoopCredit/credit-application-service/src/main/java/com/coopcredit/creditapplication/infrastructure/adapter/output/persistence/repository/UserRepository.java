package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository;

import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.CreditApplicationEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
}

