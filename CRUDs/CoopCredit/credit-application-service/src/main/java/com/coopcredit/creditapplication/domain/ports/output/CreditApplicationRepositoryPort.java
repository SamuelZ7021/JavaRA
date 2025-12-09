package com.coopcredit.creditapplication.domain.ports.output;

import com.coopcredit.creditapplication.domain.model.CreditApplication;

import java.util.Optional;

public interface CreditApplicationRepositoryPort {
    CreditApplication save(CreditApplication creditApplication);
    Optional<CreditApplication> findById(Long id);
}
