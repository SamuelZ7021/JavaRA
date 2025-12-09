package com.coopcredit.creditapplication.application.mapper;

import com.coopcredit.creditapplication.application.dto.response.CreditApplicationResponse;
import com.coopcredit.creditapplication.application.dto.response.RiskEvaluationResponse;
import com.coopcredit.creditapplication.domain.model.CreditApplication;
import com.coopcredit.creditapplication.domain.model.RiskEvaluation;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.CreditApplicationEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {AffiliateMapper.class})
public interface CreditApplicationMapper {

    @Mapping(target = "riskEvaluation", source = "riskEvaluation")
    CreditApplicationEntity toEntity(CreditApplication domain);

    @Mapping(target = "riskEvaluation", source = "riskEvaluation")
    CreditApplication toDomain(CreditApplicationEntity entity);

    @Mapping(target = "status", expression = "java(domain.getStatus().name())") // Enum a String
    CreditApplicationResponse toResponse(CreditApplication domain);

    RiskEvaluationResponse toRiskResponse(RiskEvaluation domain);
}