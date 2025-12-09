package com.coopcredit.creditapplication.infrastructure.adapter.input.rest;

import com.coopcredit.creditapplication.application.dto.request.UpdateAffiliateRequest;
import com.coopcredit.creditapplication.application.dto.response.AffiliateResponse;
import com.coopcredit.creditapplication.application.mapper.AffiliateMapper;
import com.coopcredit.creditapplication.domain.model.Affiliate;
import com.coopcredit.creditapplication.domain.ports.input.ManageAffiliateUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/affiliates")
@RequiredArgsConstructor
public class AffiliateController {

    private final ManageAffiliateUseCase affiliateUseCase;
    private final AffiliateMapper affiliateMapper;

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_AFILIADO')")
    public ResponseEntity<AffiliateResponse> getMyProfile(Authentication authentication) {
        String username = authentication.getName();
        Affiliate domain = affiliateUseCase.getAffiliateByUsername(username);
        return ResponseEntity.ok(affiliateMapper.toResponse(domain));
    }

    @PutMapping("/me")
    @PreAuthorize("hasRole('ROLE_AFILIADO')")
    public ResponseEntity<AffiliateResponse> updateMyProfile(
            @Valid @RequestBody UpdateAffiliateRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();
        Affiliate updated = affiliateUseCase.updateAffiliate(username, request);
        return ResponseEntity.ok(affiliateMapper.toResponse(updated));
    }
}