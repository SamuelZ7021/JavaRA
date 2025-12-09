package com.coopcredit.creditapplication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreditApplication {
    private Long id;
    private Affiliate affiliate;
    private BigDecimal amount;
    private Integer termMonths;
    private CreditApplicationStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RiskEvaluation riskEvaluation;
}