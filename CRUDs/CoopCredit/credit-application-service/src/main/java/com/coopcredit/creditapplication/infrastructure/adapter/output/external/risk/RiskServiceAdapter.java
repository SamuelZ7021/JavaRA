package com.coopcredit.creditapplication.infrastructure.adapter.output.external.risk;

import com.coopcredit.creditapplication.domain.model.RiskEvaluation;
import com.coopcredit.creditapplication.domain.ports.output.RiskEvaluationPort;
import com.coopcredit.creditapplication.infrastructure.adapter.output.external.risk.response.RiskServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class RiskServiceAdapter implements RiskEvaluationPort {

    private final RestClient.Builder restClientBuilder;

    @Value("${external.risk-service.url}")
    private String riskServiceUrl; // Leemos esto del application.yml

    @Override
    public RiskEvaluation evaluateRisk(String documentNumber) {
        // 1. Llamada HTTP al microservicio externo
        RiskServiceResponse response = restClientBuilder.build()
                .post()
                .uri(riskServiceUrl + "/risk-evaluation")
                .body(new RiskRequest(documentNumber))
                .retrieve()
                .body(RiskServiceResponse.class);

        int score = (response != null) ? response.getScore() : 0;

        // 2. Mapeo de DTO externo -> Dominio interno
        // Regla de negocio simple: Si score < 50, hay una razón de fallo.
        String failureReason = null;
        if (score < 50) {
            failureReason = "Low credit score from Risk Central";
        }

        return RiskEvaluation.builder()
                .score(score)
                .failureReason(failureReason)
                .createdAt(LocalDateTime.now())
                .build();
    }

    // DTO interno para el request (record)
    record RiskRequest(String document) {}
}