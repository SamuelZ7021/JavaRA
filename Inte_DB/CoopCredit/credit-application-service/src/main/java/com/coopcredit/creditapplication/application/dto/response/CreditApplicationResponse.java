package com.coopcredit.creditapplication.application.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditApplicationResponse {
    private Long id;
    private AffiliateResponse affiliate;
    private BigDecimal amount;
    private Integer termMonths;
    private String status;
    private RiskEvaluationResponse riskEvaluation;
    private LocalDateTime createdAt;
}