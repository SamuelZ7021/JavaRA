package com.coopcredit.creditapplication.infrastructure.adapter.output.persistence;


import com.coopcredit.creditapplication.application.mapper.CreditApplicationMapper;
import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.ports.output.CreditApplicationRepositoryPort;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.repository.CreditApplicationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CreditApplicationPersistenceAdapter implements CreditApplicationRepositoryPort {

    private final CreditApplicationRepository applicationRepository;
    private final CreditApplicationMapper applicationMapper;

    @Override
    public CreditApplication save(CreditApplication creditApplication) {
        // 1. Convertimos Dominio -> Entidad (JPA)
        var entity = applicationMapper.toEntity(creditApplication);

        if (entity.getRiskEvaluation() != null) {
            entity.getRiskEvaluation().setApplication(entity);
        }

        // 3. Guardamos (Ahora Hibernate sabe guardar primero el padre, obtener su ID y pasárselo al hijo)
        var saved = applicationRepository.save(entity);

        // 4. Retornamos al dominio
        return applicationMapper.toDomain(saved);
    }

    @Override
    public Optional<CreditApplication> findById(Long id) {
        return applicationRepository.findById(id)
                .map(applicationMapper::toDomain);
    }
}
