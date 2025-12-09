package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository;

import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.CreditApplicationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CreditApplicationRepository extends JpaRepository<CreditApplicationEntity, Long> {

    // Eagerly fetch affiliate to avoid N+1 when listing applications
    @EntityGraph(attributePaths = {"affiliate", "riskEvaluation"})
    Optional<CreditApplicationEntity> findById(Long id);
    List<CreditApplicationEntity> findByAffiliateUserUsername(String username);
    List<CreditApplicationEntity> findByStatus(CreditApplicationEntity.Status status);
}
