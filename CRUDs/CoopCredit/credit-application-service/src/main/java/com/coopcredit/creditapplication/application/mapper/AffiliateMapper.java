package com.coopcredit.creditapplication.application.mapper;

import com.coopcredit.creditapplication.application.dto.response.AffiliateResponse;
import com.coopcredit.creditapplication.domain.model.Affiliate;
import com.coopcredit.creditapplication.infrastructure.adapter.output.persistence.entity.AffiliateEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {UserMapper.class})
public interface AffiliateMapper {
    AffiliateEntity toEntity(Affiliate domain);
    Affiliate toDomain(AffiliateEntity entity);
    AffiliateResponse toResponse(Affiliate domain);
}