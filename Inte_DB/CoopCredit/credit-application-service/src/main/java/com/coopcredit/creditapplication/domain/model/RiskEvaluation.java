package com.coopcredit.creditapplication.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RiskEvaluation {
    private Long id;
    private Integer score;
    private String failureReason;
    private LocalDateTime createdAt;
}