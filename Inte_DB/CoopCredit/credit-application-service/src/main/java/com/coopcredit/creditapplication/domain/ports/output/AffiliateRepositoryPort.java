package com.coopcredit.creditapplication.domain.ports.output;

import com.coopcredit.creditapplication.domain.model.Affiliate;

import java.util.Optional;

public interface AffiliateRepositoryPort {
    Affiliate save(Affiliate affiliate);
    Optional<Affiliate> findById(Long id);
    Optional<Affiliate> findByEmail(String email);
    Optional<Affiliate> findByUsername(String username);
}
