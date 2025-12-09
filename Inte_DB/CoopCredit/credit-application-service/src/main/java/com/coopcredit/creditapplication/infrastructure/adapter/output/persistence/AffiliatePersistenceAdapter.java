package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence;

import com.coopcredit.creditapplication.application.mapper.AffiliateMapper;
import com.coopcredit.creditapplication.domain.model.Affiliate;
import com.coopcredit.creditapplication.domain.ports.output.AffiliateRepositoryPort;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.AffiliateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AffiliatePersistenceAdapter implements AffiliateRepositoryPort {

    private final AffiliateRepository affiliateRepository;
    private final AffiliateMapper affiliateMapper;


    @Override
    public Affiliate save(Affiliate affiliate) {
        return affiliateMapper.toDomain(
                affiliateRepository.save(affiliateMapper.toEntity(affiliate))
        );
    }

    @Override
    public Optional<Affiliate> findById(Long id) {
        return affiliateRepository.findById(id)
                .map(affiliateMapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByEmail(String email) {
        return affiliateRepository.findByEmail(email)
                .map(affiliateMapper::toDomain);
    }

    @Override
    public Optional<Affiliate> findByUsername(String username) {
        return affiliateRepository.findByUser_Username(username)
                .map(affiliateMapper::toDomain);
    }
}
