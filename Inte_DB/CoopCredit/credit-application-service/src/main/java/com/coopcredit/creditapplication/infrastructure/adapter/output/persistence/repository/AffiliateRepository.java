package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository;

import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AffiliateRepository extends JpaRepository<AffiliateEntity, Long> {
    Optional<AffiliateEntity> findByEmail(String email);
    Optional<AffiliateEntity> findByUser_Username(String username);
}
