package com.coopcredit.creditapplication.application.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreditApplicationResponse {
    private Long id;
    private AffiliateResponse affiliate; // Anidamos el DTO, no el dominio
    private BigDecimal amount;
    private Integer termMonths;
    private String status; // String es mejor que Enum para clientes externos
    private RiskEvaluationResponse riskEvaluation;
    private LocalDateTime createdAt;
}