package com.coopcredit.creditapplication.application.service;

import com.coopcredit.creditapplication.domain.exception.BusinessException;
import com.coopcredit.creditapplication.domain.exception.ResourceNotFoundException;
import com.coopcredit.creditapplication.domain.model.*;
import com.coopcredit.creditapplication.domain.ports.output.AffiliateRepositoryPort;
import com.coopcredit.creditapplication.domain.ports.output.CreditApplicationRepositoryPort;
import com.coopcredit.creditapplication.domain.ports.output.RiskEvaluationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreditApplicationServiceTest {

    @Mock
    private CreditApplicationRepositoryPort creditApplicationRepositoryPort;

    @Mock
    private AffiliateRepositoryPort affiliateRepositoryPort;

    @Mock
    private RiskEvaluationPort riskEvaluationPort;

    @InjectMocks
    private CreditApplicationService creditApplicationService;


    @Test
    public void shouldApproveCreditWhenRiskScoreUsHighAndFinancialRulesPass() {
        String username = "usuario_test";
        BigDecimal amount = new BigDecimal("200000");
        Integer term = 12;
        BigDecimal salary = new BigDecimal("10000000");

        Affiliate affiliate = Affiliate.builder()
                .user(User.builder().username(username).build())
                .salary(salary)
                .active(true)
                .build();

        RiskEvaluation highRiskScore = RiskEvaluation.builder().score(85).build();

        when(affiliateRepositoryPort.findByUsername(username)).thenReturn(Optional.of(affiliate));
        when(riskEvaluationPort.evaluateRisk(username)).thenReturn(highRiskScore);
        when(creditApplicationRepositoryPort.save(any(CreditApplication.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        CreditApplication result = creditApplicationService.createApplication(username, amount, term);

        assertEquals(CreditApplicationStatus.APPROVED, result.getStatus());
        assertEquals(85, result.getRiskEvaluation().getScore());

        verify(riskEvaluationPort).evaluateRisk(username);

    }


    @Test
    public void shouldRejectCreditWhenRiskScoreIsLow() {

        String username = "user_riesgoso";
        Affiliate affiliate = Affiliate.builder()
                .active(true)
                .salary(new BigDecimal("500000"))
                .build();

        RiskEvaluation lowRiskScore = RiskEvaluation.builder().score(40).build();

        when(affiliateRepositoryPort.findByUsername(username)).thenReturn(Optional.of(affiliate));
        when(riskEvaluationPort.evaluateRisk(username)).thenReturn(lowRiskScore);
        when(creditApplicationRepositoryPort.save(any(CreditApplication.class)))
                .thenAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        CreditApplication result = creditApplicationService.createApplication(username, new BigDecimal("100000"), 12);

        assertEquals(CreditApplicationStatus.REJECTED, result.getStatus());
        assertNotNull(result.getRiskEvaluation().getFailureReason());
    }

    @Test
    public void shouldThrowExceptionWhenMonthlyQuotaExceeds50PercentOfSalary() {
        String username = "user_poor";
        BigDecimal amount = new BigDecimal("100000000");
        Integer term = 10;
        BigDecimal salary = new BigDecimal("10000000");

        Affiliate affiliate = Affiliate.builder()
                .salary(salary)
                .active(true)
                .build();

        when(affiliateRepositoryPort.findByUsername(username)).thenReturn(Optional.of(affiliate));
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            creditApplicationService.createApplication(username, amount, term);
        });

        assertTrue(exception.getMessage().contains("exceeds 50% of your monthly income"));

        verify(riskEvaluationPort, never()).evaluateRisk(any());
        verify(creditApplicationRepositoryPort, never()).save(any());
    }

    @Test
    public void shouldThrowResourceNotFoundWhenAffiliateDoesNotExist() {
        String username = "fantasma";
        when(affiliateRepositoryPort.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            creditApplicationService.createApplication(username, BigDecimal.TEN, 12);
        });
    }


    @Test
    void shouldThrowBusinessExceptionWhenAffiliateIsInactive() {
        String username = "usuario_inactivo";
        Affiliate affiliate = Affiliate.builder()
                .user(User.builder().username(username).build())
                .active(false)
                .build();

        when(affiliateRepositoryPort.findByUsername(username)).thenReturn(Optional.of(affiliate));

        // WHEN & THEN
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            creditApplicationService.createApplication(username, BigDecimal.TEN, 12);
        });

        assertTrue(exception.getMessage().contains("is currently INACTIVE"));

        verify(riskEvaluationPort, never()).evaluateRisk(any());
        verify(creditApplicationRepositoryPort, never()).save(any());
    }

    @Test
    void shouldReturnApplicationWhenIdExists() {
        Long id = 1L;
        CreditApplication expectedApp = CreditApplication.builder().id(id).amount(BigDecimal.TEN).build();

        when(creditApplicationRepositoryPort.findById(id)).thenReturn(Optional.of(expectedApp));

        CreditApplication result = creditApplicationService.getApplicationById(id);


        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void shouldThrowResourceNotFoundWhenApplicationIdDoesNotExist() {
        Long id = 999L;
        when(creditApplicationRepositoryPort.findById(id)).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            creditApplicationService.getApplicationById(id);
        });

        assertTrue(exception.getMessage().contains("Application not found"));
    }
}