package com.coopcredit.creditapplication.domain.ports.output;

import com.coopcredit.creditapplication.domain.model.RiskEvaluation;

public interface RiskEvaluationPort {
    /**
     * Consulta el servicio externo de riesgo.
     * @param documentNumber Documento del usuario a evaluar.
     * @return RiskEvaluation Objeto de dominio con el score.
     */
    RiskEvaluation evaluateRisk(String documentNumber);
}