package com.coopcredit.creditapplication.domain.ports.output;

import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.model.CreditApplicationStatus;

import java.util.List;
import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication creditApplication);
    Optional<CreditApplication> findById(Long id);
    List<CreditApplication> findAll();
    List<CreditApplication> findByAffiliateUsername(String username);
    List<CreditApplication> findByStatus(CreditApplicationStatus status);
}
