package com.coopcredit.creditapplication.application.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class RiskEvaluationResponse {
    private Integer score;
    private String failureReason;
    private LocalDateTime createdAt;
}