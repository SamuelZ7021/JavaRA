package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence;


import com.coopcredit.creditapplication.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.model.CreditApplicationStatus;
import com.coopcredit.creditapplication.domain.ports.output.CreditApplicationRepositoryPort;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.CreditApplicationEntity;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.CreditApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditApplicationPersistenceAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationRepository applicationRepository;
    private final CreditApplicationMapper applicationMapper;

    @Override
    public CreditApplication save(CreditApplication creditApplication) {
        var entity = applicationMapper.toEntity(creditApplication);

        if (entity.getRiskEvaluation() != null) {
            entity.getRiskEvaluation().setApplication(entity);
        }

        var saved = applicationRepository.save(entity);

        return applicationMapper.toDomain(saved);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return applicationRepository.findById(id)
                .map(applicationMapper::toDomain);
    }

    @Override
    public List<CreditApplication> findAll() {
        return applicationRepository.findAll().stream()
                .map(applicationMapper::toDomain)
                .toList();
    }

    @Override
    public List<CreditApplication> findByAffiliateUsername(String username) {
        return applicationRepository.findByAffiliateUserUsername(username).stream()
                .map(applicationMapper::toDomain)
                .toList();
    }

    @Override
    public List<CreditApplication> findByStatus(CreditApplicationStatus status) {
        var entityStatus = CreditApplicationEntity.Status.valueOf(status.name());
        return applicationRepository.findByStatus(entityStatus).stream()
                .map(applicationMapper::toDomain)
                .toList();
    }
}
