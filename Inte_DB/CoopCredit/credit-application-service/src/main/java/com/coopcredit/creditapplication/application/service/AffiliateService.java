package com.coopcredit.creditapplication.application.service;

import com.coopcredit.creditapplication.application.dto.request.UpdateAffiliateRequest;
import com.coopcredit.creditapplication.domain.exception.ResourceNotFoundException;
import com.coopcredit.creditapplication.domain.model.Affiliate;
import com.coopcredit.creditapplication.domain.ports.input.ManageAffiliateUseCase;
import com.coopcredit.creditapplication.domain.ports.output.AffiliateRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AffiliateService implements ManageAffiliateUseCase {

    private final AffiliateRepositoryPort affiliateRepositoryPort;

    @Override
    @Transactional
    public Affiliate updateAffiliate(String username, UpdateAffiliateRequest request) {
        Affiliate affiliate = getAffiliateByUsername(username);

        if (request.getAddress() != null) affiliate.setAddress(request.getAddress());
        if (request.getFullName() != null) affiliate.setFullName(request.getFullName());
        if (request.getEmail() != null) {
            affiliate.setEmail(request.getEmail());
        }

        return affiliateRepositoryPort.save(affiliate);
    }

    @Override
    public Affiliate getAffiliateByUsername(String username) {
        return affiliateRepositoryPort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Affiliate not found for user: " + username));
    }
}