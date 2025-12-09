package com.coopcredit.creditapplication.domain.ports.input;

import com.coopcredit.creditapplication.domain.model.Affiliate;
import com.coopcredit.creditapplication.application.dto.request.UpdateAffiliateRequest;

public interface ManageAffiliateUseCase {
    Affiliate updateAffiliate(String username, UpdateAffiliateRequest request);
    Affiliate getAffiliateByUsername(String username);
}