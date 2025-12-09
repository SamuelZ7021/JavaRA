package com.coopcredit.creditapplication.application.service;

import com.coopcredit.creditapplication.domain.exception.BusinessException;
import com.coopcredit.creditapplication.domain.exception.ResourceNotFoundException;
import com.coopcredit.creditapplication.domain.model.*;
import com.coopcredit.creditapplication.domain.ports.input.ManageCreditApplicationUseCase;
import com.coopcredit.creditapplication.domain.ports.output.AffiliateRepositoryPort;
import com.coopcredit.creditapplication.domain.ports.output.CreditApplicationRepositoryPort;
import com.coopcredit.creditapplication.domain.ports.output.RiskEvaluationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreditApplicationService implements ManageCreditApplicationUseCase {

    private final CreditApplicationRepositoryPort applicationRepository;
    private final AffiliateRepositoryPort affiliateRepository;
    private final RiskEvaluationPort riskEvaluationPort;

    @Override
    @Transactional
    public CreditApplication createApplication(String username, BigDecimal amount, Integer termMonths) {
        log.info("Processing credit application for user: {}", username);

        // 1. Validate Affiliate exists
        Affiliate affiliate = affiliateRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format("Affiliate profile not found for user '%s'. Please complete registration.", username)
                ));

        // 2. Validate Active Status
        if (!Boolean.TRUE.equals(affiliate.getActive())) {
            throw new BusinessException(
                    String.format("Affiliate '%s' is currently INACTIVE. Contact support to reactivate your account.", username)
            );
        }

        // 3. Validate Financial Rules (Quota vs Income)
        BigDecimal monthlyQuota = amount.divide(BigDecimal.valueOf(termMonths), 2, RoundingMode.HALF_UP);
        BigDecimal maxCapacity = affiliate.getSalary().multiply(new BigDecimal("0.50")); // 50% rule

        if (monthlyQuota.compareTo(maxCapacity) > 0) {
            // ERROR ESPECÍFICO: Mostramos al usuario los números reales
            throw new BusinessException(
                    String.format("Loan rejected: Calculated monthly quota ($%s) exceeds 50%% of your monthly income ($%s). Maximum allowed quota is $%s.",
                            monthlyQuota, affiliate.getSalary(), maxCapacity)
            );
        }

        // 4. Call Risk Service
        RiskEvaluation risk = riskEvaluationPort.evaluateRisk(username);

        // 5. Determine Status
        CreditApplicationStatus status;
        if (risk.getScore() >= 70) {
            status = CreditApplicationStatus.APPROVED;
        } else {
            status = CreditApplicationStatus.REJECTED;
            // Si no hay razón del mock, ponemos una descriptiva
            if (risk.getFailureReason() == null) {
                risk.setFailureReason(
                        String.format("Risk score too low. Score obtained: %d (Minimum required: 70)", risk.getScore())
                );
            }
        }

        CreditApplication application = CreditApplication.builder()
                .affiliate(affiliate)
                .amount(amount)
                .termMonths(termMonths)
                .status(status)
                .riskEvaluation(risk)
                .build();

        return applicationRepository.save(application);
    }

    @Override
    public CreditApplication getApplicationById(Long id) {
        return applicationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Application not found with ID: " + id));
    }

    // TODO: Implement list methods based on repository queries (omitted for brevity in this step)
    @Override
    public List<CreditApplication> getApplicationsByAffiliate(String username) { return List.of(); }
    @Override
    public List<CreditApplication> getAllApplications() { return List.of(); }
}